package jp.mts.taskmanage.domain.model;

import jp.mts.base.domain.model.DomainEntity;


public class Member extends DomainEntity<MemberId> {
	private String name;
	
	public Member(MemberId memberId, String name) {
		super(memberId);
		this.name = name;
	}
	
	public MemberId memberId(){
		return id();
	}
	public String name() {
		return name;
	}
	
	public Group createGroup(
			GroupId groupId, String groupName, String description){
		Group group = new Group(groupId, memberId(), groupName, description);
		domainEventPublisher.publish(new GroupCreated(groupId, memberId()));
		return group;
	}
	
	public GroupBelonging entryAsAdministratorTo(Group group) {
		domainEventPublisher.publish(new GroupMemberEntried(group.groupId(), memberId()));
		return new GroupBelonging(group.groupId(), memberId(), true);
	}
	
	public GroupJoinApplication applyJoinTo(
			GroupJoinApplicationId applicationId, Group group) {
		return new GroupJoinApplication(
				applicationId, 
				group.groupId(), 
				memberId());
	}

	public boolean isOwnerFor(Group group) {
		return this.memberId().equals(group.ownerMemberId());
	}
	
	void setName(String name) {
		this.name = name;
	}
	
	public boolean cancel(GroupJoinApplication application) {
		if(!memberId().equals(application.applicationMemberId())) {
			return false;
		}
		application.cancel();
		return true;
	}
	
}
