package jp.mts.taskmanage.domain.model.task;

import java.util.Date;

import jp.mts.taskmanage.domain.model.member.MemberId;

public class TaskBuilder {
	
	private Task task;

	public TaskBuilder(Task task) {
		this.task = task;
	}

	public TaskBuilder setName(String name) {
		this.task.setName(name);
		return this;
	}
	public TaskBuilder setStatus(TaskStatus status) {
		this.task.setStatus(status);
		return this;
	}
	public TaskBuilder setDeadline(Date deadline) {
		this.task.setDeadline(deadline);
		return this;
	}
	public TaskBuilder setAssigned(MemberId assigned) {
		this.task.setAssignedMemberId(assigned);
		return this;
	}
	public TaskBuilder setMemo(String memo) {
		this.task.setMemo(memo);
		return this;
	}
	
	public Task get() {
		return this.task;
	}
}
