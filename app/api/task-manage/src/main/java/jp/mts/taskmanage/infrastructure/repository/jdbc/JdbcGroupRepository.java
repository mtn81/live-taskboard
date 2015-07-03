package jp.mts.taskmanage.infrastructure.repository.jdbc;

import java.util.UUID;

import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.GroupRepository;
import jp.mts.taskmanage.infrastructure.repository.jdbc.model.GroupModel;

import org.springframework.stereotype.Repository;

@Repository
public class JdbcGroupRepository implements GroupRepository {

	@Override
	public GroupId newGroupId() {
		return new GroupId(UUID.randomUUID().toString().replaceAll("\\-", ""));
	}

	@Override
	public void save(Group group) {
		GroupModel groupModel = GroupModel.findFirst("group_id = ?", group.groupId().value());
		if(groupModel == null){
			groupModel = new GroupModel();
		}
		groupModel.set(
			"group_id", group.groupId().value(),
			"name", group.name(),
			"description", group.description())
			.save();
	}

	@Override
	public Group findById(GroupId groupId) {
		GroupModel groupModel = GroupModel.findFirst("group_id = ?", groupId.value());
		return new Group(
				new GroupId(groupModel.getString("group_id")), 
				groupModel.getString("name"), 
				groupModel.getString("description"));
	}
	
}
