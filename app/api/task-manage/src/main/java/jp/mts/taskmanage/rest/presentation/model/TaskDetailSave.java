package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.domain.model.Task;

import org.hibernate.validator.constraints.Length;

public class TaskDetailSave {
	
	private static TaskAppService taskAppService;
	
	public static void setTaskAppService(TaskAppService taskAppService) {
		TaskDetailSave.taskAppService = taskAppService;
	}

	// input
	@Length(max=1000)
	private String memo;

	public void setMemo(String memo) {
		this.memo = memo;
	}

	// output
	private Task task;

	public String getTaskId() {
		if(task == null) return null;
		return task.taskId().value();
	}
	
	// proccess
	public void update(
			String modifierMemberId,
			String groupId, 
			String taskId) {
		
		task = taskAppService.modifyTaskDetail(
				modifierMemberId, groupId, taskId, memo);
	}

}
