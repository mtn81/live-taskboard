package jp.mts.taskmanage.rest.presentation.model;

import com.sun.org.apache.bcel.internal.generic.ReturnaddressType;

import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.application.query.GroupBelongingSearchQuery;
import jp.mts.taskmanage.domain.model.group.Group;

public class GroupLoad {
	
	//required services
	private static GroupAppService groupAppService;
	
	public static void setGroupAppService(GroupAppService groupAppService) {
		GroupLoad.groupAppService = groupAppService;
	}

	//output
	private Group result;
	private boolean isAdmin;
	
	public String getGroupId() {
		if(result == null) return null;
		return result.groupId().value();
	}
	public String getName() {
		if(result == null) return null;
		return result.name();
	}
	public String getDescription() {
		if(result == null) return null;
		return result.description();
	}
	public boolean isAdmin() {
		return isAdmin;
	}

	public void loadBelongingGroup(String memberId, String groupId) {
		groupAppService.findBelongingGroup(groupId, memberId, (group, isAdmin) -> {
			this.result = group;
			this.isAdmin = isAdmin;
		});
	}
}
