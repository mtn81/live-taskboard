package jp.mts.taskmanage.websocket;

import jp.mts.taskmanage.domain.model.Group;

public class GroupAvailableNotification {
	
	private Group group;
	
	public GroupAvailableNotification(Group group) {
		this.group = group;
	}

	public String getGroupId(){
		return group.groupId().value();
	}
	public String getGroupName(){
		return group.name();
	}
}
