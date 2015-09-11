package jp.mts.taskmanage.rest.presentation.model;

import java.util.Date;

import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.domain.model.Task;

public class TaskSave {

	// input
	private String taskName;
	private String assigned;
	private Date deadline;

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public void setAssigned(String assigned) {
		this.assigned = assigned;
	}
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	
	// output
	private Task task;

	public String getTaskId() {
		return task.taskId().value();
	}
	
	// proccess
	public void create(String groupId, TaskAppService taskAppService) {
		task = taskAppService.registerTask(
				groupId, taskName, assigned, deadline);
	}

}
