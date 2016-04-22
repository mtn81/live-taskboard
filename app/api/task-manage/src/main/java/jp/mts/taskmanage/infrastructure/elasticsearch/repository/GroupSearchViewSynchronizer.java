package jp.mts.taskmanage.infrastructure.elasticsearch.repository;

import static org.apache.commons.collections.CollectionUtils.union;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.constantScoreQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import jp.mts.base.infrastructure.elasticsearch.AbstractElasticSearchAccessor;
import jp.mts.base.util.ListUtils;
import jp.mts.taskmanage.domain.model.group.Group;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.group.join.GroupJoin;
import jp.mts.taskmanage.domain.model.member.Member;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class GroupSearchViewSynchronizer extends AbstractElasticSearchAccessor {

	@Autowired
	public GroupSearchViewSynchronizer(TransportClient transportClient) {
		super("task-manage", "view_group_search", transportClient);
	}
	
	public void syncFrom(Group group) {
		
		Map<String, Object> ownerMember = prepareGet("member", group.ownerMemberId().value()).get().getSource();

		List<String> applicants = toList(
			prepareSearch("group_join")
				.setQuery(constantScoreQuery(
					boolQuery()
						.must(termQuery("group_id", group.id().value()))
						.mustNot(termsQuery("status", "CANCELLED", "ACCEPTED"))
				))
				.setVersion(true)
				.get().getHits(), 
			(hit, source) -> (String)source.get("applicant_id")
		);
		List<String> members = toList(
			prepareSearch("member")
				.setQuery(constantScoreQuery(
					nestedQuery("belongings", 
						termQuery("belongings.group_id", group.id().value())
					)
				))
				.get().getHits(), 
			(hit, source) -> hit.getId() 
		);
		
		prepareIndex(group.id().value())
			.setSource(
				"group_id", group.id().value(),
				"group_name", group.name(),
				"group_description", group.description(),
				"owner_id", group.ownerMemberId().value(),
				"owner_name", ownerMember == null ? "" : (String)ownerMember.get("name"),
				"owner_type", ownerMember == null ? "" : (String)ownerMember.get("type"),
				"applicants", applicants,
				"members", members
			)
			.get();
		
		//TODO verify owner name and applicants and members unchanged
	}
	@SuppressWarnings("unchecked")
	public void syncFrom(Member member) {
		
		BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();

		Map<String, SearchHit> ownerHits = toIdMap(
			prepareSearch()
				.setQuery(constantScoreQuery(
					termQuery("owner_id", member.id().value())
				))
				.setVersion(true)
				.get().getHits(),
			hit -> hit);
		Map<String, SearchHit> belongingHits = toIdMap(
			prepareSearch()
				.setQuery(constantScoreQuery(
					boolQuery()
						.should(termsQuery("group_id", ListUtils.convert(member.groupBelongings(), gb -> gb.groupId().value())))
						.should(termQuery("members", member.id().value()))
				))
				.setVersion(true)
				.get().getHits(),
			hit -> hit);
		
		//同じドキュメントをversion付きでbulk更新すると競合するので、マージしておく
		((Collection<String>)union(ownerHits.keySet(), belongingHits.keySet()))
			.forEach((String id) -> {
				SearchHit ownerHit = ownerHits.get(id);
				SearchHit belongingHit = belongingHits.get(id);
				UpdateRequest updateRequest = updateRequest(id);
				
				if (ownerHit != null && belongingHit != null) {

					String groupId = belongingHit.getId();
					List<String> memberIds = (List<String>)belongingHit.getSource().get("members");
					updateRequest.doc(
						"owner_name", member.name(),
						"owner_type", member.registerType(),
						"members", mergedMemberIds(groupId, memberIds, member)
					).version(ownerHit.getVersion());

				} else if(ownerHit != null) {

					updateRequest.doc(
						"owner_name", member.name(),
						"owner_type", member.registerType()
					).version(ownerHit.getVersion());

				} else if(belongingHit != null) {

					String groupId = belongingHit.getId();
					List<String> memberIds = (List<String>)belongingHit.getSource().get("members");
					updateRequest.doc(
						"members", mergedMemberIds(groupId, memberIds, member)
					).version(belongingHit.getVersion());
				}
				
				bulkRequestBuilder.add(updateRequest);
			});
		
		if(bulkRequestBuilder.numberOfActions() <= 0) return;
		bulkRequestBuilder.get();
	}
	
	public void syncFrom(GroupJoin groupJoin) {
		GetResponse getResponse = prepareGet(groupJoin.groupId().value()).get();
		List<String> applicantIds = (List<String>)getResponse.getSource().get("applicants");

		if (!applicantIds.contains(groupJoin.applicationMemberId().value())) {
			prepareUpdate(groupJoin.groupId().value())
				.setDoc(
					"applicants", ListUtils.appended(applicantIds, groupJoin.applicationMemberId().value())
				)
				.setVersion(getResponse.getVersion())
				.get();
		}
	}

	private List<String> mergedMemberIds(
			String groupId, 
			List<String> memberIds, 
			Member member) {

		List<String> merged = memberIds == null ? 
				Lists.newArrayList() : Lists.newArrayList(memberIds);
		
		if (member.belongsTo(new GroupId(groupId))) {
			if (!memberIds.contains(member.id().value())) {
				merged.add(member.id().value());
			}
		} else {
			if (memberIds.contains(member.id().value())) {
				merged.remove(member.id().value());
			}
		}
		
		return merged;
	}

}
