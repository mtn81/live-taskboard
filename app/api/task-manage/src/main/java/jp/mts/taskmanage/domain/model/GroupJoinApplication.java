package jp.mts.taskmanage.domain.model;


import java.util.Date;

import jp.mts.base.domain.model.DomainEntity;

public class GroupJoinApplication extends DomainEntity<GroupJoinApplicationId> {

	private GroupId groupId;
	private MemberId applicantMemberId;
	private GroupJoinApplicationStatus status;
	private Date applied;

	public GroupJoinApplication(
			GroupJoinApplicationId id,
			GroupId groupId, 
			MemberId applicantMemberId) {

		super(id);
		this.groupId = groupId;
		this.applicantMemberId = applicantMemberId;
		this.status = GroupJoinApplicationStatus.APPLIED;
		this.applied = calendar.systemDate();
	}
	
	public GroupId groupId() {
		return groupId;
	}
	public MemberId applicationMemberId() {
		return applicantMemberId;
	}
	public GroupJoinApplicationStatus status() {
		return status;
	}
	public Date applied() {
		return applied;
	}
	public void cancel() {
		setStatus(GroupJoinApplicationStatus.CANCELLED);
	}
	
	void setStatus(GroupJoinApplicationStatus status) {
		this.status = status;
	}
	void setApplied(Date applied) {
		this.applied = applied;
	}

}
