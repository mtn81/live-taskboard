package jp.mts.taskmanage.infrastructure.jdbc.repository;

import static java.util.stream.Collectors.toList;

import java.util.List;

import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.domain.model.Task;
import jp.mts.taskmanage.domain.model.TaskBuilder;
import jp.mts.taskmanage.domain.model.TaskId;
import jp.mts.taskmanage.domain.model.TaskRepository;
import jp.mts.taskmanage.domain.model.TaskStatus;
import jp.mts.taskmanage.infrastructure.jdbc.model.TaskModel;

import org.javalite.activejdbc.Model;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTaskRepository implements TaskRepository {

	@Override
	public List<Task> findByGroupId(GroupId groupId) {
		return TaskModel.find("group_id=?", groupId.value()).stream()
				.map(taskModel -> toDomain(taskModel))
				.collect(toList());
	}

	@Override
	public void save(Task task) {
		Model taskModel = TaskModel.findFirst("task_id=?", task.taskId().value());
		if (taskModel == null) {
			taskModel = new TaskModel();
		}
		taskModel.set(
				"task_id", task.taskId().value(),
				"group_id", task.groupId().value(),
				"status", task.status().name(),
				"name", task.name(),
				"deadline", task.deadline(),
				"assigned", task.assignedMemberId().value())
				.saveIt();
	}


	private Task toDomain(Model taskModel) {
		return new TaskBuilder(
			new Task(
				new GroupId(taskModel.getString("group_id")), 
				new TaskId(taskModel.getString("task_id")),
				taskModel.getString("name")))
			.setStatus(TaskStatus.valueOf(taskModel.getString("status")))
			.setDeadline(taskModel.getDate("deadline"))
			.setAssigned(new MemberId(taskModel.getString("assigned")))
			.get();
	}
}
