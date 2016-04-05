package jp.mts.taskmanage.domain.model.member;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;
import jp.mts.taskmanage.domain.model.group.GroupId;

@DomainEventConfig(eventType="mts:taskmanage/GroupMemberLeaved")
public class GroupMemberLeaved extends DomainEvent {
	
	private GroupId groupId;
	private MemberId memberId;

	public GroupMemberLeaved(GroupId groupId, MemberId memberId) {
		this.groupId = groupId;
		this.memberId = memberId;
	}

	public String getGroupId() {
		return groupId.value();
	}
	public String getMemberId() {
		return memberId.value();
	}
	
}
