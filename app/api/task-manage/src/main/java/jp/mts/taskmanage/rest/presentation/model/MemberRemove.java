package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.GroupAppService;

public class MemberRemove {
	
	//required services
	private static GroupAppService groupAppService;
	
	public static void setGroupAppService(GroupAppService groupAppService) {
		MemberRemove.groupAppService = groupAppService;
	}

	//input

	//output
	private String memberId;
	
	public String getMemberId() {
		return memberId;
	}

	//process
	public void remove(String groupId, String memberId) {
		groupAppService.removeGroupBelonging(groupId, memberId);
		this.memberId = memberId;
	}
}
