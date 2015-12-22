package jp.mts.taskmanage.domain.model;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;

@DomainEventConfig(eventType="mts:taskmanage/MemberJoinAccepted")
public class MemberJoinAccepted extends DomainEvent {
	
	private MemberId memberId;
	private GroupId groupId;

	public MemberJoinAccepted(MemberId memberId, GroupId groupId) {
		this.memberId = memberId;
		this.groupId = groupId;
	}

	public String getMemberId() {
		return memberId.value();
	}

	public String getGroupId() {
		return groupId.value();
	}

}
