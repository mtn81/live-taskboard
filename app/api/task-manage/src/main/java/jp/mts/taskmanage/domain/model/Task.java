package jp.mts.taskmanage.domain.model;

import java.util.Date;

public class Task {

	private TaskId taskId;
	private GroupId groupId;
	private TaskStatus taskStatus;
	private String name;
	private Date deadline;
	private MemberId assignedMemberId;
	
	Task(){}
	
	public Task(
			GroupId groupId, 
			TaskId taskId, 
			String name, 
			MemberId assignedMemberId, 
			Date deadline) {
		this.groupId = groupId;
		this.taskId = taskId;
		this.taskStatus = TaskStatus.TODO; 
		this.name = name;
		this.assignedMemberId = assignedMemberId;
		this.deadline = deadline;
	}

	public GroupId groupId(){
		return groupId;
	}
	public TaskId taskId(){
		return taskId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		return true;
	}
}
