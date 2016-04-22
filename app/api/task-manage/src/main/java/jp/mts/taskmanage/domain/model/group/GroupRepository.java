package jp.mts.taskmanage.domain.model.group;

import java.util.Optional;

public interface GroupRepository {
	GroupId newGroupId();
	Optional<Group> findById(GroupId id);
	void save(Group group);
	void remove(Group group);
}
