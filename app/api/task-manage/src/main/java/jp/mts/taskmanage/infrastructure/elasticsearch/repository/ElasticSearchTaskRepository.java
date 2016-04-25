package jp.mts.taskmanage.infrastructure.elasticsearch.repository;

import static org.elasticsearch.index.query.QueryBuilders.constantScoreQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import jp.mts.base.infrastructure.elasticsearch.AbstractElasticSearchRepository;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.member.MemberId;
import jp.mts.taskmanage.domain.model.task.Task;
import jp.mts.taskmanage.domain.model.task.TaskBuilder;
import jp.mts.taskmanage.domain.model.task.TaskId;
import jp.mts.taskmanage.domain.model.task.TaskRepository;
import jp.mts.taskmanage.domain.model.task.TaskStatus;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.get.GetField;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ElasticSearchTaskRepository 
	extends AbstractElasticSearchRepository
	implements TaskRepository {

	@Autowired
	public ElasticSearchTaskRepository(TransportClient transportClient) {
		super("task-manage", "task", transportClient);
	}

	@Override
	public Optional<Task> findById(GroupId groupId, TaskId taskId) {
		GetResponse getResponse = prepareGet(taskId.value() + "@" + groupId.value()).get();
		if(getResponse == null || getResponse.getSource() == null) return Optional.empty();
		
		Map<String, Object> source = getResponse.getSource();
		return Optional.of(toDomain(source));
	}

	@Override
	public void save(Task task) {
		prepareIndex(idOf(task))
			.setSource(
				"task_id", task.taskId().value(),
				"group_id", task.groupId().value(),
				"status", task.status().name(),
				"name", task.name(),
				"assigned", task.assignedMemberId().value(),
				"memo", task.memo(),
				"deadline", date(task.deadline())
			)
			.get();
		
		await();
	}

	@Override
	public void remove(Task task) {
		prepareDelete(idOf(task)).get();
	}

	@Override
	public List<Task> findByGroupId(GroupId groupId) {
		return toList(
			prepareSearch()
				.setQuery(constantScoreQuery(
					termQuery("group_id", groupId.value())
				))
				.get().getHits(),
			(hit, source) -> toDomain(source)
		);
	}

	@Override
	public TaskId newTaskId(GroupId groupId) {
		UpdateResponse sequenceResponse = prepareUpdate("sequence", "task_id", groupId.value())
			.setScript(new Script("ctx._source.value += 1", ScriptType.INLINE, "groovy", null))
			.setRetryOnConflict(10)
			.setFields("value")
			.setUpsert("value", 1)
			.get();
		
		GetField getField = sequenceResponse.getGetResult().getFields().get("value");
		return new TaskId("TASK-" + String.valueOf(getField.getValue()));
	}
	
	private String idOf(Task task) {
		return task.id().value() + "@" + task.groupId().value();
	}
	private Task toDomain(Map<String, Object> source) {
		return new TaskBuilder(
					new Task(
						new GroupId((String)source.get("group_id")), 
						new TaskId((String)source.get("task_id")),
						(String)source.get("name"),
						new MemberId((String)source.get("assigned")),
						date((String)source.get("deadline"))
					)
				)
				.setStatus(TaskStatus.valueOf((String)source.get("status")))
				.setMemo((String)source.get("memo"))
				.get();
	}
}
