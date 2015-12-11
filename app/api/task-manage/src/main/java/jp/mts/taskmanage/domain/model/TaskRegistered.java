package jp.mts.taskmanage.domain.model;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;

@DomainEventConfig(eventType="mts:taskmanage/TaskRegistered")
public class TaskRegistered extends DomainEvent {
	
	public TaskId taskId;
	public GroupId groupId;

	public TaskRegistered(Task task) {
		this.taskId = task.taskId();
		this.groupId = task.groupId();
	}
	
	public TaskId getTaskId() {
		return taskId;
	}
	public GroupId getGroupId() {
		return groupId;
	}
}
