package jp.mts.taskmanage.domain.model;

import jp.mts.base.domain.model.DomainEvent;

public class MemberJoinAccepted extends DomainEvent {
	
	private MemberId memberId;
	private GroupId groupId;

	public MemberJoinAccepted(MemberId memberId, GroupId groupId) {
		this.memberId = memberId;
		this.groupId = groupId;
	}

	public MemberId memberId() {
		return memberId;
	}

	public GroupId groupId() {
		return groupId;
	}

}
