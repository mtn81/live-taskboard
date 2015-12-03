package jp.mts.taskmanage.domain.model;

import java.util.Date;

import jp.mts.base.domain.model.DomainEntity;


public class Group extends DomainEntity<GroupId>{
	
	private MemberId ownerMemberId;
	private String name;
	private String description;

	public Group(GroupId groupId, MemberId ownerMemberId, String name, String description) {
		super(groupId);
		this.ownerMemberId = ownerMemberId;
		setName(name);
		setDescription(description);
	}

	public GroupId groupId() {
		return id();
	}
	public MemberId ownerMemberId() {
		return ownerMemberId;
	}
	public String name() {
		return name;
	}
	public String description() {
		return description;
	}
	public void changeAttributes(String name, String description){
		setName(name);
		setDescription(description);
	}
	
	public boolean accept(GroupJoinApplication application) {
		if(!groupId().equals(application.groupId())){
			return false;
		}
		
		application.accept();
		domainEventPublisher.publish(
				new MemberJoinAccepted(application.applicationMemberId(), application.groupId()));
		return true;
	}
	public boolean reject(GroupJoinApplication application){
		if(!groupId().equals(application.groupId())){
			return false;
		}
		application.reject();
		return true;
	}
	
	public Task createTask(
			TaskId taskId, 
			String taskName, 
			Member assignedMember,
			Date deadline) {

		Task task = new Task(
				groupId(), 
				taskId,
				taskName, 
				assignedMember.memberId(), 
				deadline);
		return task;
	}
	
	void setName(String name) {
		if(name == null) throw new IllegalArgumentException();
		this.name = name;
	}
	void setDescription(String description) {
		this.description = description;
	}

}
