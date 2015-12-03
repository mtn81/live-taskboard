package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.GroupAppService;

public class MemberChange {
	
	//required services
	private static GroupAppService groupAppService;
	
	public static void setGroupAppService(GroupAppService groupAppService) {
		MemberChange.groupAppService = groupAppService;
	}

	//input
	private boolean admin;
	
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	//output
	public boolean isAdmin() {
		return admin;
	}

	//process
	public void change(String groupId, String memberId) {
		
		groupAppService.changeGroupBelonging(groupId, memberId, admin);
	}
}
