package jp.mts.taskmanage.domain.model;

import java.util.List;

public interface TaskRepository {

	List<Task> findByGroupId(GroupId groupId);
	void save(Task task);
}
