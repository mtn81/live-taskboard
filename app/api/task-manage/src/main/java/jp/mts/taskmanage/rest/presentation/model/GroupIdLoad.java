package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.domain.model.group.Group;

public class GroupIdLoad {
	
	//required services
	private static GroupAppService groupAppService;
	
	public static void setGroupAppService(GroupAppService groupAppService) {
		GroupIdLoad.groupAppService = groupAppService;
	}

	//output
	private Group group;
	
	public String getGroupId() {
		return group.groupId().value();
	}

	public void load(String groupId) {
		group = groupAppService.findById(groupId);
	}
}
