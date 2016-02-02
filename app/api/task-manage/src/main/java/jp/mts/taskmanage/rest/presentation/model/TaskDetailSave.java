package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.domain.model.Task;

public class TaskDetailSave {
	
	private static TaskAppService taskAppService;
	
	public static void setTaskAppService(TaskAppService taskAppService) {
		TaskDetailSave.taskAppService = taskAppService;
	}

	// input
	private String memo;

	public void setMemo(String memo) {
		this.memo = memo;
	}

	// output
	private Task task;

	public String getTaskId() {
		return task.taskId().value();
	}
	
	// proccess
	public void update(
			String groupId, 
			String taskId) {
		
		task = taskAppService.modifyTaskDetail(
				groupId, taskId, memo);
	}

}
