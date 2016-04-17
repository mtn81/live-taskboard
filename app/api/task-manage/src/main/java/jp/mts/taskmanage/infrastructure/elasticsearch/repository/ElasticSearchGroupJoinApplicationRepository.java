package jp.mts.taskmanage.infrastructure.elasticsearch.repository;

import java.util.Optional;
import java.util.UUID;

import jp.mts.base.infrastructure.elasticsearch.AbstractElasticSearchRepository;
import jp.mts.base.util.MapUtils;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplication;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationBuilder;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationId;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationRepository;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationStatus;
import jp.mts.taskmanage.domain.model.member.MemberId;

import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ElasticSearchGroupJoinApplicationRepository 
	extends AbstractElasticSearchRepository
	implements GroupJoinApplicationRepository {

	@Autowired
	public ElasticSearchGroupJoinApplicationRepository(TransportClient transportClient) {
		super("task-manage", "group_join", transportClient);
	}

	@Override
	public Optional<GroupJoinApplication> findById(GroupJoinApplicationId id) {
		return searchDomainById(id.value(), source -> {
			return new GroupJoinApplicationBuilder(
				new GroupJoinApplication(
						new GroupJoinApplicationId((String)source.get("application_id")), 
						new GroupId((String)source.get("group_id")), 
						new MemberId((String)source.get("applicant_id"))))
				.setStatus(GroupJoinApplicationStatus.valueOf((String)source.get("status")))
				.setApplied(dateTime((String)source.get("applied_time")))
				.get();
		});
	}

	@Override
	public void save(GroupJoinApplication groupJoin) {
		save(groupJoin.id().value(), groupJoin, groupJoin.groupId().value(),
			gj -> MapUtils.pairs(
				"application_id", gj.id().value(),
				"group_id", gj.groupId().value(),
				"applicant_id", gj.applicationMemberId().value(),
				"status", gj.status().name(),
				"applied_time", dateTime(gj.applied()))
		);
	}

	@Override
	public void remove(GroupJoinApplication entity) {
		delete(entity.id().value());
	}

	@Override
	public GroupJoinApplicationId newId() {
		return new GroupJoinApplicationId(UUID.randomUUID().toString());
	}

}
