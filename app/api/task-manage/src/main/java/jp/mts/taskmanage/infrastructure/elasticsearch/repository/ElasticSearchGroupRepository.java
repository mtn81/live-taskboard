package jp.mts.taskmanage.infrastructure.elasticsearch.repository;

import java.util.Optional;
import java.util.UUID;

import jp.mts.base.infrastructure.elasticsearch.AbstractElasticSearchRepository;
import jp.mts.base.util.MapUtils;
import jp.mts.taskmanage.domain.model.group.Group;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.group.GroupRepository;
import jp.mts.taskmanage.domain.model.member.MemberId;

import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ElasticSearchGroupRepository 
	extends AbstractElasticSearchRepository
	implements GroupRepository {
	
	private GroupOwnerSynchronizer groupOwnerSynchronizer;
	
	@Autowired
	public ElasticSearchGroupRepository(
		GroupOwnerSynchronizer groupOwnerSynchronizer,
		TransportClient transportClient) {

		super("task-manage", "group", transportClient);
		this.groupOwnerSynchronizer = groupOwnerSynchronizer;
	}

	@Override
	public Optional<Group> findById(GroupId id) {
		return getDomain(id.value(), source -> {
			return new Group(
				new GroupId((String)source.get("group_id")), 
				new MemberId((String)source.get("owner_member_id")),
				(String)source.get("name"), 
				(String)source.get("description"));
		});
	}

	@Override
	public void save(Group group) {
		save(group.id().value(), group, gp -> MapUtils.pairs(
			"group_id", gp.groupId().value(),
			"owner_member_id", gp.ownerMemberId().value(),
			"name", gp.name(),
			"description", gp.description())
		);
		groupOwnerSynchronizer.syncOnGroupChanged(group);
	}

	@Override
	public void remove(Group group) {
		delete(group.id().value());
	}

	@Override
	public GroupId newGroupId() {
		return new GroupId(UUID.randomUUID().toString().replaceAll("\\-", ""));
	}
}
