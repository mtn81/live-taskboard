package jp.mts.taskmanage.domain.model.group.join;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;

@DomainEventConfig(eventType="mts:taskmanage/GroupJoinRejected")
public class GroupJoinRejected extends DomainEvent {
	
	private GroupJoin joinApplication;
	public GroupJoinRejected(GroupJoin joinApplication) {
		this.joinApplication = joinApplication;
	}

	public String getMemberId() {
		return joinApplication.applicationMemberId().value();
	}

	public String getGroupId() {
		return joinApplication.groupId().value();
	}

}
