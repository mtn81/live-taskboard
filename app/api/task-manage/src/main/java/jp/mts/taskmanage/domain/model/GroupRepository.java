package jp.mts.taskmanage.domain.model;


public interface GroupRepository {

	GroupId newGroupId();
	void save(Group group);
	Group findById(GroupId groupId);
}
