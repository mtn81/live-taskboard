package jp.mts.taskmanage.infrastructure.jdbc.repository;

import java.util.List;

import jp.mts.base.infrastructure.jdbc.repository.AbstractCompositeIdJdbcDomainRepository;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.member.MemberId;
import jp.mts.taskmanage.domain.model.task.Task;
import jp.mts.taskmanage.domain.model.task.TaskBuilder;
import jp.mts.taskmanage.domain.model.task.TaskId;
import jp.mts.taskmanage.domain.model.task.TaskRepository;
import jp.mts.taskmanage.domain.model.task.TaskStatus;
import jp.mts.taskmanage.infrastructure.jdbc.model.TaskIdGeneratorModel;
import jp.mts.taskmanage.infrastructure.jdbc.model.TaskModel;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

@Repository
public class JdbcTaskRepository 
	extends AbstractCompositeIdJdbcDomainRepository<Task, TaskModel>
	implements TaskRepository {

	@Override
	public List<Task> findByGroupId(GroupId groupId) {
		return findList("group_id=?", groupId.value());
	}

	@Override
	public TaskId newTaskId(GroupId groupId) {
		TaskIdGeneratorModel model = TaskIdGeneratorModel.findFirst("group_id=?", groupId.value());
		if (model == null) {
			model = new TaskIdGeneratorModel().set(
					"group_id", groupId.value(),
					"task_id_num", 0);
		}
		int newTaskIdNumber = model.getInteger("task_id_num") + 1;
		model.set("task_id_num", newTaskIdNumber).saveIt();
		return new TaskId("TASK-" + newTaskIdNumber);
	}


	@Override
	protected List<String> idColumnNames() {
		return Lists.newArrayList("group_id", "task_id");
	}

	@Override
	protected Task toDomain(TaskModel taskModel) {
		return new TaskBuilder(
			new Task(
				new GroupId(taskModel.getString("group_id")), 
				new TaskId(taskModel.getString("task_id")),
				taskModel.getString("name"),
				new MemberId(taskModel.getString("assigned")),
				taskModel.getDate("deadline")))
			.setStatus(TaskStatus.valueOf(taskModel.getString("status")))
			.setMemo(taskModel.getString("memo"))
			.get();
	}

	@Override
	protected void toModel(TaskModel taskModel, Task task) {
		taskModel.set(
				"task_id", task.taskId().value(),
				"group_id", task.groupId().value(),
				"status", task.status().name(),
				"name", task.name(),
				"assigned", task.assignedMemberId().value(),
				"memo", task.memo());
		taskModel.setDate("deadline", task.deadline());
	}
	
	
}
