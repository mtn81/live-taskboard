package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.domain.model.Task;

public class TaskDetailLoad {
	private static TaskAppService taskAppService;
	
	public static void setTaskAppService(TaskAppService taskAppService) {
		TaskDetailLoad.taskAppService = taskAppService;
	}

	//output
	private Task task;

	public String getTaskId() {
		return task.taskId().value();
	}

	public String getMemo() {
		return task.memo();
	}
	
	//process
	public void load(String groupId, String taskId) {
		task = taskAppService.loadById(groupId, taskId);
	}

}
