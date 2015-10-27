package jp.mts.taskmanage.domain.model;

import java.util.List;


public interface GroupRepository {

	GroupId newGroupId();
	void save(Group group);
	Group findById(GroupId groupId);
	List<Group> findByIds(List<GroupId> groupIds);
	void remove(Group group);
	
}
