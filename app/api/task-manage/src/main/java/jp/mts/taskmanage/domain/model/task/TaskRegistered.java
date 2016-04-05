package jp.mts.taskmanage.domain.model.task;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;

@DomainEventConfig(eventType="mts:taskmanage/TaskRegistered")
public class TaskRegistered extends DomainEvent {
	
	private Task task;

	public TaskRegistered(Task task) {
		this.task = task;
	}
	
	public String getTaskId() {
		return task.taskId().value();
	}
	public String getGroupId() {
		return task.groupId().value();
	}
	public String getAssigned() {
		return task.assignedMemberId().value();
	}
}
