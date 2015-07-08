package jp.mts.taskmanage.infrastructure.repository.jdbc;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.GroupRepository;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.infrastructure.repository.jdbc.model.GroupModel;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

@Repository
public class JdbcGroupRepository implements GroupRepository {

	@Override
	public GroupId newGroupId() {
		return new GroupId(UUID.randomUUID().toString().replaceAll("\\-", ""));
	}

	@Override
	public void save(Group group) {
		GroupModel groupModel = GroupModel.findFirst("group_id = ?", group.groupId().value());
		if(groupModel == null) groupModel = new GroupModel(); 
		
		groupModel.set(
			"group_id", group.groupId().value(),
			"owner_member_id", group.ownerMemberId().value(),
			"name", group.name(),
			"description", group.description())
			.save();
	}

	@Override
	public Group findById(GroupId groupId) {
		GroupModel groupModel = GroupModel.findFirst("group_id = ?", groupId.value());
		return toDomain(groupModel);
	}
	
	private Group toDomain(GroupModel groupModel){
		Group group = new Group(
				new GroupId(groupModel.getString("group_id")), 
				new MemberId(groupModel.getString("owner_member_id")),
				groupModel.getString("name"), 
				groupModel.getString("description"));

		return group;
	}

	@Override
	public List<Group> findByIds(List<GroupId> groupIds) {
		if(groupIds.isEmpty()){
			return Lists.newArrayList();
		}
		SqlInClause<GroupId> inClause = new SqlInClause<>("group_id", groupIds, groupId -> groupId.value());
		return GroupModel
				.where(inClause.condition(), inClause.params())
					.orderBy("group_id")
				.stream()
					.map(groupModel -> toDomain((GroupModel)groupModel))
					.collect(Collectors.toList());
	}
}
