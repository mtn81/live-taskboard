package jp.mts.taskmanage.domain.model.group;

import java.util.Date;

import jp.mts.base.domain.model.DomainEntity;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinAccepted;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplication;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinRejected;
import jp.mts.taskmanage.domain.model.member.Member;
import jp.mts.taskmanage.domain.model.member.MemberId;
import jp.mts.taskmanage.domain.model.task.Task;
import jp.mts.taskmanage.domain.model.task.TaskId;
import jp.mts.taskmanage.domain.model.task.TaskRegistered;
import jp.mts.taskmanage.domain.model.task.TaskRemoved;


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
		domainEventPublisher.publish(new GroupJoinAccepted(application));
		return true;
	}

	public boolean reject(GroupJoinApplication application){
		if(!groupId().equals(application.groupId())){
			return false;
		}
		application.reject();
		domainEventPublisher.publish(new GroupJoinRejected(application));
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
		
		domainEventPublisher.publish(new TaskRegistered(task));
		
		return task;
	}

	public boolean removeTask(Task task) {
		if (!task.groupId().equals(groupId())) {
			return false;
		}
		domainEventPublisher.publish(new TaskRemoved(task));
		return true;
	}
	
	void setName(String name) {
		if(name == null) throw new IllegalArgumentException();
		this.name = name;
	}
	void setDescription(String description) {
		this.description = description;
	}

}
