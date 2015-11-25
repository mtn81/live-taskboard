package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.GroupAppService;

public class MemberRoleChange {
	
	//required services
	private static GroupAppService groupAppService;
	
	public static void setGroupAppService(GroupAppService groupAppService) {
		MemberRoleChange.groupAppService = groupAppService;
	}

	//input
	private String groupId;
	
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	//output
	private boolean admin;
	
	public boolean isAdmin() {
		return admin;
	}

	//process
	public void changeToAdmin(String memberId) {
		groupAppService.entryGroup(groupId, memberId, true);
		admin = true;
	}

	public void changeToNormal(String memberId) {
		groupAppService.entryGroup(groupId, memberId, false);
		admin = false;
	}
}
