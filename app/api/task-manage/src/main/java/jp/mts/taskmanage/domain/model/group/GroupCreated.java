package jp.mts.taskmanage.domain.model.group;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;
import jp.mts.taskmanage.domain.model.member.MemberId;

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
