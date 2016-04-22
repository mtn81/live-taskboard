package jp.mts.taskmanage.domain.model.task;

import java.util.List;
import java.util.Optional;

import jp.mts.taskmanage.domain.model.group.GroupId;

public interface TaskRepository {

	TaskId newTaskId(GroupId groupId);
	Optional<Task> findById(GroupId groupId, TaskId taskId);
	List<Task> findByGroupId(GroupId groupId);
	void save(Task task);
	void remove(Task task);
}
