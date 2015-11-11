package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.GroupJoinAppService;

public class GroupJoinCancel {
	
	private static GroupJoinAppService groupJoinAppService;
	
	public static void setGroupJoinAppService(GroupJoinAppService groupJoinAppService) {
		GroupJoinCancel.groupJoinAppService = groupJoinAppService;
	}

	//input

	//output
	private String joinApplicationId;

	public String getJoinApplicationId() {
		return joinApplicationId;
	}

	public void cancel(String applicantId, String joinApplicationId) {
		groupJoinAppService.cancelJoin(applicantId, joinApplicationId);
		this.joinApplicationId = joinApplicationId;
	}
	
}
