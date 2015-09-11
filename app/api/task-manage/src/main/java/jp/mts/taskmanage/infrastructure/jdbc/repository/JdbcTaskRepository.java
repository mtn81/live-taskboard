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
import jp.mts.taskmanage.infrastructure.jdbc.model.TaskIdGeneratorModel;
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
		Model taskModel = find(task.taskId(), task.groupId());
		if (taskModel == null) {
			taskModel = new TaskModel();
		}
		taskModel.set(
				"task_id", task.taskId().value(),
				"group_id", task.groupId().value(),
				"status", task.status().name(),
				"name", task.name(),
				"assigned", task.assignedMemberId().value());
		taskModel.setDate("deadline", task.deadline());
		taskModel.saveIt();
	}


	private Task toDomain(Model taskModel) {
		return new TaskBuilder(
			new Task(
				new GroupId(taskModel.getString("group_id")), 
				new TaskId(taskModel.getString("task_id")),
				taskModel.getString("name"),
				new MemberId(taskModel.getString("assigned")),
				taskModel.getDate("deadline")))
			.setStatus(TaskStatus.valueOf(taskModel.getString("status")))
			.get();
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
	public Task findById(GroupId groupId, TaskId taskId) {
		Model taskModel = find(taskId, groupId);
		if (taskModel == null)  return null;
		return toDomain(taskModel);
	}

	@Override
	public void remove(Task task) {
		TaskModel.delete("task_id=? and group_id=?", 
				task.taskId().value(), task.groupId().value());
	}
	
	private TaskModel find(TaskId taskId, GroupId groupId) {
		return TaskModel.findFirst(
				"task_id=? and group_id=?", taskId.value(), groupId.value());
	}
}
