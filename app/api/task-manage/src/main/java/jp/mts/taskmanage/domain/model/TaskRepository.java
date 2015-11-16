package jp.mts.taskmanage.domain.model;

import java.util.List;

import jp.mts.base.domain.model.CompositeId;
import jp.mts.base.domain.model.DomainRepository;

public interface TaskRepository extends DomainRepository<CompositeId, Task>{

	List<Task> findByGroupId(GroupId groupId);
	TaskId newTaskId(GroupId groupId);
}
