package jp.mts.taskmanage.domain.model;

import java.util.Date;

import jp.mts.base.domain.model.CompositeId;
import jp.mts.base.domain.model.DomainEntity;

public class Task extends DomainEntity<CompositeId>{

	private TaskStatus taskStatus;
	private String name;
	private Date deadline;
	private MemberId assignedMemberId;
	
	public Task(
			GroupId groupId, 
			TaskId taskId, 
			String name, 
			MemberId assignedMemberId, 
			Date deadline) {
		super(CompositeId.of(groupId, taskId));
		this.taskStatus = TaskStatus.TODO; 
		this.name = name;
		this.assignedMemberId = assignedMemberId;
		this.deadline = deadline;
	}

	public GroupId groupId(){
		return (GroupId)id().value().get(0);
	}
	public TaskId taskId(){
		return (TaskId)id().value().get(1);
	}
	public TaskStatus status() {
		return taskStatus;
	}
	public String name() {
		return name;
	}
	public Date deadline() {
		return deadline;
	}
	public MemberId assignedMemberId() {
		return assignedMemberId;
	}
	
	public void changeSummary(
			String taskName, 
			Member assignedMember, 
			Date deadline,
			TaskStatus status) {
		
		setName(taskName);
		setAssignedMemberId(assignedMember.memberId());
		setDeadline(deadline);
		setStatus(status);
	}
	
	void setStatus(TaskStatus status) {
		this.taskStatus = status;
	}
	void setName(String name) {
		this.name = name;
	}
	void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	void setAssignedMemberId(MemberId assignedMemberId) {
		this.assignedMemberId = assignedMemberId;
	}

}
