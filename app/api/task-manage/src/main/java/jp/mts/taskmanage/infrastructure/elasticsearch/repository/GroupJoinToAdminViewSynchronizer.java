package jp.mts.taskmanage.infrastructure.elasticsearch.repository;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.constantScoreQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.mts.base.infrastructure.elasticsearch.AbstractElasticSearchAccessor;
import jp.mts.taskmanage.domain.model.group.Group;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplication;
import jp.mts.taskmanage.domain.model.member.Member;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class GroupJoinToAdminViewSynchronizer extends AbstractElasticSearchAccessor {

	@Autowired
	public GroupJoinToAdminViewSynchronizer(TransportClient transportClient) {
		super("task-manage", "view_group_join_to_admin", transportClient);
	}
	
	public void syncFrom(GroupJoinApplication groupJoin) {

		Map<String, Object> group = prepareGet("group", groupJoin.groupId().value()).get().getSource();
		if(group == null) return;
		Map<String, Object> applicant = prepareGet("member", groupJoin.applicationMemberId().value()).get().getSource();

		List<String> adminMembers = toList( 
			prepareSearch("member")
				.setQuery(constantScoreQuery(
					nestedQuery("belongings", boolQuery()
						.must(termQuery("belongings.group_id", groupJoin.groupId().value()))
						.must(termQuery("belongings.admin", true))
					).scoreMode("none")
				))
				.get()
				.getHits(),
			(hit, source) -> (String)source.get("member_id")
		);
		
		prepareIndex(groupJoin.id().value())
			.setSource(
				"application_id", groupJoin.id().value(),
				"applied_time", dateTime(groupJoin.applied()),
				"status", groupJoin.status(),
				"group_id", groupJoin.groupId().value(),
				"group_name", group.get("name") ,
				"applicant_id", groupJoin.applicationMemberId().value(),
				"applicant_type", applicant == null ? "" : applicant.get("name"),
				"applicant_name", applicant == null ? "" : applicant.get("type"),
				"admin_members", adminMembers)
			.get();
		
		//TODO verify groupName and adminMembers unchanged
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
	public void syncFrom(Member member) {
		
		BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
		prepareSearch()
			.setQuery(constantScoreQuery(
				termQuery("applicant_id", member.id().value())
			))
			.setVersion(true)
			.get()
			.getHits()
			.forEach(hit -> {
				bulkRequestBuilder.add(
					updateRequest(hit.getId())
						.doc(
							"applicant_name", member.name(),
							"applicant_type", member.registerType().name()
						)
						.version(hit.getVersion())
				);
			});

		prepareSearch()
			.setQuery(constantScoreQuery(
				termQuery("admin_members", member.id().value())
			))
			.setVersion(true)
			.get()
			.getHits()
			.forEach(hit -> {
				bulkRequestBuilder.add(
					updateRequest(hit.getId())
						.doc(
							"admin_members", mergedAdminMembers(
									(String)hit.getSource().get("groupId"),
									(List<String>)hit.getSource().get("admin_members"),
									member)
						)
						.version(hit.getVersion())
				);
			});

		if(bulkRequestBuilder.numberOfActions() <= 0) return;
		bulkRequestBuilder.get();
	}
	private List<String> mergedAdminMembers(
			String groupId,
			List<String> oldAdminMembers, 
			Member member) {

		List<String> result = oldAdminMembers.stream()
				.filter(m -> !m.equals(member.id().value()))
				.collect(Collectors.toList());

		if (member.belongsAsAdmin(new GroupId(groupId))) {
			result.add(member.id().value());
		}
		return result;
	}
}
