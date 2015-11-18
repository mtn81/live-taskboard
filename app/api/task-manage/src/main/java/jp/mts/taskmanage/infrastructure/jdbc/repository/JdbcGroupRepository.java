package jp.mts.taskmanage.infrastructure.jdbc.repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jp.mts.base.infrastructure.jdbc.SqlInClause;
import jp.mts.base.infrastructure.jdbc.repository.AbstractSimpleJdbcDomainRepository;
import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.GroupRepository;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupMemberModel;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupModel;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

@Repository
public class JdbcGroupRepository 
	extends AbstractSimpleJdbcDomainRepository<
		GroupId,
		Group, 
		GroupModel>
	implements GroupRepository {

	@Override
	public GroupId newGroupId() {
		return new GroupId(UUID.randomUUID().toString().replaceAll("\\-", ""));
	}


	@Override
	public void remove(Group group) {
		
		GroupMemberModel
			.delete("group_id=?", group.groupId().value());
		
		super.remove(group);
	}

	@Override
	protected String idColumnName() {
		return "group_id";
	}

	@Override
	protected Group toDomain(GroupModel groupModel) {
		return new Group(
				new GroupId(groupModel.getString("group_id")), 
				new MemberId(groupModel.getString("owner_member_id")),
				groupModel.getString("name"), 
				groupModel.getString("description"));
		
	}

	@Override
	protected void toModel(GroupModel model, Group group) {

		model.set(
			"group_id", group.groupId().value(),
			"owner_member_id", group.ownerMemberId().value(),
			"name", group.name(),
			"description", group.description());
	}
}
