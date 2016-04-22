package jp.mts.taskmanage.domain.model.group.join;


import java.util.Date;

import jp.mts.base.domain.model.DomainEntity;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.member.MemberId;

public class GroupJoin extends DomainEntity<GroupJoinId> {

	private GroupId groupId;
	private MemberId applicantMemberId;
	private GroupJoinStatus status;
	private Date applied;

	public GroupJoin(
			GroupJoinId id,
			GroupId groupId, 
			MemberId applicantMemberId) {

		super(id);
		this.groupId = groupId;
		this.applicantMemberId = applicantMemberId;
		this.status = GroupJoinStatus.APPLIED;
		this.applied = calendar.systemDate();
	}
	
	public GroupId groupId() {
		return groupId;
	}
	public MemberId applicationMemberId() {
		return applicantMemberId;
	}
	public GroupJoinStatus status() {
		return status;
	}
	public Date applied() {
		return applied;
	}
	public void cancel() {
		setStatus(GroupJoinStatus.CANCELLED);
	}
	public void reject() {
		setStatus(GroupJoinStatus.REJECTED);
	}
	public void accept() {
		setStatus(GroupJoinStatus.ACCEPTED);
	}

	
	void setStatus(GroupJoinStatus status) {
		this.status = status;
	}
	void setApplied(Date applied) {
		this.applied = applied;
	}


}
