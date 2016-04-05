package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.GroupJoinAppService;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplication;

public class GroupJoinApply {
	
	private static GroupJoinAppService groupJoinAppService;
	
	public static void setGroupJoinAppService(GroupJoinAppService groupJoinAppService) {
		GroupJoinApply.groupJoinAppService = groupJoinAppService;
	}

	//input
	private String groupId;

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	//output
	private GroupJoinApplication groupJoin;

	public String getJoinApplicationId() {
		return groupJoin.id().value();
	}

	public void apply(String memberId) {
		this.groupJoin = groupJoinAppService.applyJoin(groupId, memberId);
	}
	
}
