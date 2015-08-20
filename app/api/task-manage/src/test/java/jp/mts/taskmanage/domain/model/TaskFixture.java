package jp.mts.taskmanage.domain.model;

import java.util.Date;

public class TaskFixture {
	
	private Task task;
	
	public TaskFixture() {
		this("g01", "t01");
	}
	public TaskFixture(String taskId) {
		this("g01", taskId);
	}
	public TaskFixture(String groupId, String taskId) {
		task = new Task(new GroupId(groupId), new TaskId(taskId), "task01");
	}

	public Task get() {
		return task;
	}

	public TaskFixture setStatus(TaskStatus status) {
		task.setStatus(status);
		return this;
	}
	public TaskFixture setName(String name) {
		task.setName(name);
		return this;
	}
	public TaskFixture setDeadline(Date deadline) {
		task.setDeadline(deadline);
		return this;
	}
	public TaskFixture setAssigned(String memberId) {
		task.setAssignedMemberId(new MemberId(memberId));
		return this;
	}
	
	

}
