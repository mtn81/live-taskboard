package jp.mts.taskmanage.domain.model;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;

@DomainEventConfig(eventType="mts:taskmanage/TaskRemoved")
public class TaskRemoved extends DomainEvent {
	
	public TaskId taskId;
	public GroupId groupId;

	public TaskRemoved(Task task) {
		this.taskId = task.taskId();
		this.groupId = task.groupId();
	}
	
	public String getTaskId() {
		return taskId.value();
	}
	public String getGroupId() {
		return groupId.value();
	}
}
