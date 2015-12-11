package jp.mts.taskmanage.domain.model;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;

@DomainEventConfig(eventType="mts:taskmanage/TaskModified")
public class TaskModified extends DomainEvent {
	
	public TaskId taskId;
	public GroupId groupId;

	public TaskModified(Task task) {
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
