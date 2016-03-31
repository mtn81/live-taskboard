package jp.mts.taskmanage.domain.model;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;

@DomainEventConfig(eventType="mts:taskmanage/GroupJoinAccepted")
public class GroupJoinAccepted extends DomainEvent {
	
	private GroupJoinApplication joinApplication;
	public GroupJoinAccepted(GroupJoinApplication joinApplication) {
		this.joinApplication = joinApplication;
	}

	public String getMemberId() {
		return joinApplication.applicationMemberId().value();
	}

	public String getGroupId() {
		return joinApplication.groupId().value();
	}

}
