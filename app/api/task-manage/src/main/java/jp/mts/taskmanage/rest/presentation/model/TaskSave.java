package jp.mts.taskmanage.rest.presentation.model;

import java.util.Date;

import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.domain.model.Task;
import jp.mts.taskmanage.domain.model.TaskStatus;

public class TaskSave {

	// input
	private String taskName;
	private String assigned;
	private Date deadline;
	private String status;

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public void setAssigned(String assigned) {
		this.assigned = assigned;
	}
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	// output
	private Task task;

	public String getTaskId() {
		return task.taskId().value();
	}
	
	// proccess
	public void create(
			String groupId, 
			TaskAppService taskAppService) {

		task = taskAppService.registerTask(
				groupId, taskName, assigned, deadline);
	}
	public void update(
			String modifierMemberId,
			String groupId, 
			String taskId,
			TaskAppService taskAppService) {
		
		task = taskAppService.modifyTask(
				modifierMemberId,
				groupId, 
				taskId, 
				taskName, 
				assigned, 
				deadline, 
				TaskStatus.valueOf(status.toUpperCase()));
	}

}
