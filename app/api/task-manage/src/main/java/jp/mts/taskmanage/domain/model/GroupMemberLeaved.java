package jp.mts.taskmanage.domain.model;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;

@DomainEventConfig(eventType="mts:taskmanage/GroupMemberLeaved")
public class GroupMemberLeaved extends DomainEvent {
	
	private GroupId groupId;
	private MemberId memberId;

	public GroupMemberLeaved(GroupId groupId, MemberId memberId) {
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
