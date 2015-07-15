package jp.mts.taskmanage.domain.model;

import jp.mts.base.domain.model.DomainEvent;

public class GroupCreated extends DomainEvent {

	private GroupId groupId;
	private MemberId creator;

	public GroupCreated(GroupId groupId, MemberId creator) {
		this.groupId = groupId;
		this.creator = creator;
	}

	public GroupId groupId() {
		return groupId;
	}

	public MemberId creator() {
		return creator;
	}
	
}
