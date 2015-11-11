package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.application.GroupJoinAppService;
import jp.mts.taskmanage.domain.model.GroupJoinApplication;

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

	public String getJoinId() {
		return groupJoin.id().value();
	}

	public void apply(String memberId) {
		this.groupJoin = groupJoinAppService.applyJoin(groupId, memberId);
	}
	
}
