package jp.mts.taskmanage.domain.model.task;

import java.util.List;

import jp.mts.base.domain.model.CompositeId;
import jp.mts.base.domain.model.DomainRepository;
import jp.mts.taskmanage.domain.model.group.GroupId;

public interface TaskRepository extends DomainRepository<CompositeId, Task>{

	List<Task> findByGroupId(GroupId groupId);
	TaskId newTaskId(GroupId groupId);
}
