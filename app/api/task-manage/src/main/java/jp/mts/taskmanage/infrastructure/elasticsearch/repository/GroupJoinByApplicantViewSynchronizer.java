package jp.mts.taskmanage.infrastructure.elasticsearch.repository;

import static org.elasticsearch.index.query.QueryBuilders.constantScoreQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.Map;

import jp.mts.base.infrastructure.elasticsearch.AbstractElasticSearchAccessor;
import jp.mts.taskmanage.domain.model.group.Group;
import jp.mts.taskmanage.domain.model.group.join.GroupJoin;
import jp.mts.taskmanage.domain.model.member.Member;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupJoinByApplicantViewSynchronizer extends AbstractElasticSearchAccessor {

	@Autowired
	public GroupJoinByApplicantViewSynchronizer(TransportClient transportClient) {
		super("task-manage", "view_group_join_by_applicant", transportClient);
	}

	public void syncFrom(Member member) {
		
		BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
		prepareSearch()
			.setQuery(constantScoreQuery(
				termQuery("owner_id", member.id().value())
			))
			.setVersion(true)
			.get()
			.getHits()
			.forEach(hit -> {
				bulkRequestBuilder.add(
					updateRequest(hit.getId())
						.doc(
							"owner_type", member.registerType().name(),
							"owner_name", member.name()
						)
						.version(hit.getVersion())
				);
			});

		if(bulkRequestBuilder.numberOfActions() <= 0) return;
		bulkRequestBuilder.get();
	}
	public void syncFrom(Group group) {
		
		BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
		prepareSearch()
			.setQuery(constantScoreQuery(
				termQuery("group_id", group.id().value())
			))
			.setVersion(true)
			.get()
			.getHits()
			.forEach(hit -> {
				bulkRequestBuilder.add(
					updateRequest(hit.getId())
						.doc(
							"group_name", group.name()
						)
						.version(hit.getVersion())
				);
			});

		if(bulkRequestBuilder.numberOfActions() <= 0) return;
		bulkRequestBuilder.get();
	}
	public void syncFrom(GroupJoin groupJoin) {
		
		Map<String, Object> group = prepareGet("group", groupJoin.groupId().value()).get().getSource();
		if (group == null) return;

		Map<String, Object> groupOwner = prepareGet("member", (String)group.get("owner_member_id")).get().getSource();
		
		prepareIndex(groupJoin.id().value())
			.setSource(
				"application_id", groupJoin.id().value(),
				"applied_time", dateTime(groupJoin.applied()),
				"status", groupJoin.status(),
				"applicant_id", groupJoin.applicationMemberId().value(),
				"group_id", groupJoin.groupId().value(),
				"group_name", group.get("name") ,
				"owner_id", group.get("owner_member_id"),
				"owner_name", groupOwner == null ? "" : groupOwner.get("name"),
				"owner_type", groupOwner == null ? "" : groupOwner.get("type"))
			.get();
		
		//TODO verify group and member unchanged
	}

}
