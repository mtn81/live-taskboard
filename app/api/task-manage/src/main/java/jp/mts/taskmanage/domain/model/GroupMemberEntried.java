package jp.mts.taskmanage.domain.model;

import jp.mts.base.domain.model.DomainEvent;

public class GroupMemberEntried extends DomainEvent {
	
	private GroupId groupId;
	private MemberId memberId;

	public GroupMemberEntried(GroupId groupId, MemberId memberId) {
		this.groupId = groupId;
		this.memberId = memberId;
	}

	public GroupId getGroupId() {
		return groupId;
	}
	public MemberId getMemberId() {
		return memberId;
	}
	
}
