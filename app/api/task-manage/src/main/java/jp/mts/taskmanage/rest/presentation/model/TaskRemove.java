package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.domain.model.Task;

public class TaskRemove {
	
	private Task task;
	
	public String getTaskId() {
		return task.taskId().value();
	}

	public void remove(
			String groupId, 
			String taskId,
			TaskAppService taskAppService) {
		
		this.task = taskAppService.removeTask(groupId, taskId);
	}
}
