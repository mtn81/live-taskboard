package jp.mts.taskmanage.domain.model.task;

import java.util.Date;

import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.member.MemberId;
import jp.mts.taskmanage.domain.model.task.Task;
import jp.mts.taskmanage.domain.model.task.TaskId;
import jp.mts.taskmanage.domain.model.task.TaskStatus;

public class TaskFixture {
	
	private Task task;
	
	public TaskFixture() {
		this("g01", "t01");
	}
	public TaskFixture(String taskId) {
		this("g01", taskId);
	}
	public TaskFixture(String groupId, String taskId) {
		task = new Task(new GroupId(groupId), new TaskId(taskId), "task01", new MemberId("m01"), new Date());
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
	public TaskFixture setMemo(String memo) {
		task.setMemo(memo);
		return this;
	}
	
	

}
