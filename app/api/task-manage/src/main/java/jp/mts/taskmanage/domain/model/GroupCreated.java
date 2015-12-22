package jp.mts.taskmanage.domain.model;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;

@DomainEventConfig(eventType="mts:taskmanage/GroupCreated")
public class GroupCreated extends DomainEvent {

	private GroupId groupId;
	private MemberId creator;

	public GroupCreated(GroupId groupId, MemberId creator) {
		this.groupId = groupId;
		this.creator = creator;
	}

	public String getGroupId() {
		return groupId.value();
	}

	public String getCreatorMemberId() {
		return creator.value();
	}
	
}
