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
	
	public String getTaskId() {
		return taskId.value();
	}
	public String getGroupId() {
		return groupId.value();
	}
}
