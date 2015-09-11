package jp.mts.taskmanage.domain.model;

import java.util.List;

public interface TaskRepository {

	List<Task> findByGroupId(GroupId groupId);
	void save(Task task);
	TaskId newTaskId(GroupId groupId);
	Task findById(GroupId groupId, TaskId taskId);
	void remove(Task task);
}
