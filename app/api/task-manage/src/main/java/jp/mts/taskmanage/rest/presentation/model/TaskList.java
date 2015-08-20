package jp.mts.taskmanage.rest.presentation.model;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.domain.model.Task;
import jp.mts.taskmanage.domain.model.TaskStatus;

public class TaskList {

	List<Task> tasks = new ArrayList<>();
	
	public List<TaskView> getTodo() {
		return tasksOfState(TaskStatus.TODO);
	}
	public List<TaskView> getDoing() {
		return tasksOfState(TaskStatus.DOING);
	}
	public List<TaskView> getDone() {
		return tasksOfState(TaskStatus.DONE);
	}
	private List<TaskView> tasksOfState(TaskStatus status) {
		return tasks.stream()
				.filter(task -> task.status() == status)
				.map(task -> new TaskView(task))
				.collect(toList());
	}

	public void loadTasks(String groupId, TaskAppService taskAppService) {
		tasks = taskAppService.findTasksByGroup(groupId);
	}

	public static class TaskView {

		private Task task;
		
		public TaskView(Task task) {
			this.task = task;
		}

		public String getTaskId() {
			return task.taskId().value();
		}
		public String getTaskName() {
			return task.name();
		}
		public Date getDeadline() {
			return task.deadline();
		}
		public String getAssigned() {
			return task.assignedMemberId().value();
		}
	}


}
