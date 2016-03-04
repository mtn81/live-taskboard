package jp.mts.taskmanage.domain.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jp.mts.base.domain.model.DomainEntity;


public class Member extends DomainEntity<MemberId> {
	private String name;
	private String email;
	private MemberRegisterType registerType;
	private Set<GroupBelonging> groupBelongings = new HashSet<>();
	
	public Member(
			MemberId memberId, 
			String name,
			MemberRegisterType registerType) {
		super(memberId);
		this.name = name;
		this.registerType = registerType;
	}
	
	public MemberId memberId(){
		return id();
	}
	public String name() {
		return name;
	}
	public String email() {
		return email;
	}

	public MemberRegisterType registerType() {
		return registerType;
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

	public boolean owns(Group group) {
		return memberId().equals(group.ownerMemberId());
	}

	public void entryTo(Group group, boolean admin) {
		addGroupBelonging(new GroupBelonging(group.groupId(), admin));
		domainEventPublisher.publish(new GroupMemberEntried(group.groupId(), memberId()));
	}
	public LeaveResult leave(Group group) {
		if(owns(group))
			return LeaveResult.OWNER_ERROR;

		removeGroupBelonging(group.groupId());
		domainEventPublisher.publish(new GroupMemberLeaved(group.groupId(), memberId()));
		return LeaveResult.SUCCESS;
	}
	
	
	public void changeToAdmin(Group group) {
		addGroupBelonging(new GroupBelonging(group.groupId(), true));
	}

	public boolean changeToNormal(Group group) {
		if(owns(group)) return false;

		addGroupBelonging(new GroupBelonging(group.groupId(), false));
		return true;
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
	public GroupBelonging belongingOf(GroupId groupId) {
		return groupBelongings.stream().filter(belonging -> {
			return belonging.groupId().equals(groupId);
		}).findFirst().get();
	}
	
	public boolean isOwnerFor(Group group) {
		return this.memberId().equals(group.ownerMemberId());
	}

	public boolean editGroup(
			Group group, 
			String name, 
			String description) {
		if(!belongsAsAdmin(group.groupId())) {
			return false;
		}
		group.changeAttributes(name, description);
		return true;
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
	void setEmail(String email) {
		this.email = email;
	}
	void setGroupBelongings(Collection<GroupBelonging> groupBelongings) {
		this.groupBelongings = new HashSet<>(groupBelongings);
	}
	void addGroupBelonging(GroupBelonging groupBelongings) {
		if(belongsTo(groupBelongings.groupId())) {
			this.groupBelongings.remove(belongingOf(groupBelongings.groupId()));
		}

		this.groupBelongings.add(groupBelongings);
	}
	void addGroupBelongings(Collection<GroupBelonging> groupBelongings) {
		groupBelongings.forEach(groupBelonging -> 
			addGroupBelonging(groupBelonging));
	}
	private void removeGroupBelonging(GroupId groupId) {
		if(!belongsTo(groupId)) {
			throw new IllegalArgumentException();
		}
		this.groupBelongings.remove(belongingOf(groupId));
	}
	
	public enum LeaveResult {
		SUCCESS, NOT_ADMIN_ERROR, OWNER_ERROR
	}

	public void changeAttributes(
			String name, 
			String email) {
		setName(name);
		setEmail(email);
	}

}
