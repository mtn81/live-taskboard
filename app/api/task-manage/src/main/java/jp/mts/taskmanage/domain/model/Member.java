package jp.mts.taskmanage.domain.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jp.mts.base.domain.model.DomainEntity;


public class Member extends DomainEntity<MemberId> {
	private String name;
	private Set<GroupBelonging> groupBelongings = new HashSet<>();
	
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
	public Set<GroupBelonging> groupBelongings() {
		return new HashSet<>(groupBelongings);
	}
	
	public Group createGroup(
			GroupId groupId, String groupName, String description){
		Group group = new Group(groupId, memberId(), groupName, description);

		domainEventPublisher.publish(new GroupCreated(groupId, memberId()));
		return group;
	}

	public void entryTo(Group group) {
		entryTo(group, false);
	}

	public void entryTo(Group group, boolean admin) {
		addGroupBelonging(new GroupBelonging(group.groupId(), admin));
		domainEventPublisher.publish(new GroupMemberEntried(group.groupId(), memberId()));
	}
	
	public GroupJoinApplication applyJoinTo(
			GroupJoinApplicationId applicationId, Group group) {

		return new GroupJoinApplication(
				applicationId, 
				group.groupId(), 
				memberId());
	}
	public boolean cancel(GroupJoinApplication application) {
		if(!memberId().equals(application.applicationMemberId())) {
			return false;
		}
		application.cancel();
		return true;
	}
	public boolean reject(GroupJoinApplication application){
		if(!belongsAsAdmin(application.groupId())){
			return false;
		}
		application.reject();
		return true;
	}
	public boolean accept(GroupJoinApplication application) {
		if(!belongsAsAdmin(application.groupId())){
			return false;
		}
		
		application.accept();
		domainEventPublisher.publish(
				new MemberJoinAccepted(application.applicationMemberId(), application.groupId()));
		return true;
	}
	public boolean belongsAsAdmin(GroupId groupId) {
		return groupBelongings.stream().anyMatch(belonging -> {
			return belonging.groupId().equals(groupId)
					&& belonging.isAdmin();
		});
	}
	public boolean belongsTo(GroupId groupId) {
		return groupBelongings.stream().anyMatch(belonging -> {
			return belonging.groupId().equals(groupId);
		});
	}
	
	public boolean isOwnerFor(Group group) {
		return this.memberId().equals(group.ownerMemberId());
	}
	
	public boolean remove(Group group) {
		if(!belongsAsAdmin(group.groupId())) {
			return false;
		}
		domainEventPublisher.publish(new GroupRemoved());
		return true;
	}
	
	void setName(String name) {
		this.name = name;
	}
	void setGroupBelongings(Collection<GroupBelonging> groupBelongings) {
		this.groupBelongings = new HashSet<>(groupBelongings);
	}
	void addGroupBelonging(GroupBelonging groupBelongings) {
		if(belongsTo(groupBelongings.groupId()))
			throw new IllegalArgumentException();

		this.groupBelongings.add(groupBelongings);
	}
	void addGroupBelongings(Collection<GroupBelonging> groupBelongings) {
		groupBelongings.forEach(groupBelonging -> 
			addGroupBelonging(groupBelonging));
	}

	
}
