package jp.mts.taskmanage.infrastructure.elasticsearch.repository;

import java.util.Optional;
import java.util.UUID;

import jp.mts.base.infrastructure.elasticsearch.AbstractElasticSearchRepository;
import jp.mts.base.infrastructure.elasticsearch.ElasticSearchActionAssurer;
import jp.mts.base.util.MapUtils;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.group.join.GroupJoin;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinBuilder;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinId;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinRepository;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinStatus;
import jp.mts.taskmanage.domain.model.member.MemberId;

import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ElasticSearchGroupJoinApplicationRepository 
	extends AbstractElasticSearchRepository
	implements GroupJoinRepository {
	
	private GroupJoinByApplicantViewSynchronizer groupJoinByApplicantViewSynchronizer;
	private GroupJoinToAdminViewSynchronizer groupJoinToAdminViewSynchronizer;
	private GroupSearchViewSynchronizer groupSearchViewSynchronizer;
	private ElasticSearchActionAssurer elasticSearchActionAssurer;

	@Autowired
	public ElasticSearchGroupJoinApplicationRepository(
			GroupJoinByApplicantViewSynchronizer groupJoinByApplicantViewSynchronizer,
			GroupJoinToAdminViewSynchronizer groupJoinToAdminViewSynchronizer,
			GroupSearchViewSynchronizer groupSearchViewSynchronizer,
			TransportClient transportClient) {

		super("task-manage", "group_join", transportClient);
		this.groupJoinByApplicantViewSynchronizer = groupJoinByApplicantViewSynchronizer;
		this.groupJoinToAdminViewSynchronizer = groupJoinToAdminViewSynchronizer;
		this.groupSearchViewSynchronizer = groupSearchViewSynchronizer;
		this.elasticSearchActionAssurer = new ElasticSearchActionAssurer("task-manage", 5, transportClient);
	}

	@Override
	public Optional<GroupJoin> findById(GroupJoinId id) {
		return getDomain(id.value(), source -> {
			return new GroupJoinBuilder(
				new GroupJoin(
						new GroupJoinId((String)source.get("application_id")), 
						new GroupId((String)source.get("group_id")), 
						new MemberId((String)source.get("applicant_id"))))
				.setStatus(GroupJoinStatus.valueOf((String)source.get("status")))
				.setApplied(dateTime((String)source.get("applied_time")))
				.get();
		});
	}

	@Override
	public void save(GroupJoin groupJoin) {
		save(groupJoin.id().value(), groupJoin, 
			gj -> MapUtils.pairs(
				"application_id", gj.id().value(),
				"group_id", gj.groupId().value(),
				"applicant_id", gj.applicationMemberId().value(),
				"status", gj.status().name(),
				"applied_time", dateTime(gj.applied()))
		);

		elasticSearchActionAssurer.ensureAction("group_join_save_sync_" + groupJoin.id().value(), () -> {
			this.groupJoinByApplicantViewSynchronizer.syncFrom(groupJoin);
			this.groupJoinToAdminViewSynchronizer.syncFrom(groupJoin);
			this.groupSearchViewSynchronizer.syncFrom(groupJoin);
		});
	}


	@Override
	public GroupJoinId newId() {
		return new GroupJoinId(UUID.randomUUID().toString());
	}

}
