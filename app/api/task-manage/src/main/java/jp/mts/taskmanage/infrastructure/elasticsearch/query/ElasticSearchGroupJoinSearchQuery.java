package jp.mts.taskmanage.infrastructure.elasticsearch.query;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.constantScoreQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.List;

import jp.mts.base.infrastructure.elasticsearch.AbstractElasticSearchAccessor;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinStatus;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class ElasticSearchGroupJoinSearchQuery 
	extends AbstractElasticSearchAccessor
	implements GroupJoinSearchQuery {

	@Autowired
	public ElasticSearchGroupJoinSearchQuery(TransportClient transportClient) {
		super("task-manage", transportClient);
	}

	@Override
	public List<ByApplicantResult> byApplicant(String memberId) {
		
		SearchHits hits = prepareSearch("view_group_join_by_applicant")
			.setQuery(constantScoreQuery(
				boolQuery()
					.must(termQuery("applicant_id", memberId))
					.mustNot(termQuery("status", "CANCELLED"))
			))
			.addSort("applied_time", SortOrder.ASC)
			.get()
			.getHits();

		if (hits.getTotalHits() <= 0) return Lists.newArrayList();

		return toList(hits, (hit, join) -> 
				new ByApplicantResult(
					hit.getId(),
					(String)join.get("group_id"), 
					(String)join.get("group_name"), 
					(String)join.get("owner_name"), 
					(String)join.get("owner_type"), 
					dateTime((String)join.get("applied_time")), 
					GroupJoinStatus.valueOf((String)join.get("status"))));
	}

	@Override
	public List<ByAdminResult> acceptableByAdmin(String memberId) {
		return byAdmin(memberId, GroupJoinStatus.APPLIED);
	}

	@Override
	public List<ByAdminResult> rejectedByAdmin(String memberId) {
		return byAdmin(memberId, GroupJoinStatus.REJECTED);
	}
	
	@Override
	public List<AppliableGroupResult> appliableGroups(
			String memberId, String query) {
		
		return toList(
			prepareSearch("view_group_search")
				.setQuery(
					boolQuery()
						.mustNot(termQuery("applicants", memberId))
						.mustNot(termQuery("members", memberId))
						.should(matchQuery("group_name", query))
						.should(matchQuery("group_description", query))
				)
				.get().getHits(),
			(hit, group) -> {
				return new AppliableGroupResult(
						(String)group.get("group_id"),
						(String)group.get("group_name"),
						(String)group.get("owner_name"),
						(String)group.get("owner_type"),
						(String)group.get("group_description"));
			}
		);
	}

	private List<ByAdminResult> byAdmin(String memberId, GroupJoinStatus status) {

		SearchHits hits = prepareSearch("view_group_join_to_admin")
			.setQuery(constantScoreQuery(
				boolQuery()
					.must(termQuery("status", status.name()))
					.must(termQuery("admin_members", memberId))
			))
			.addSort("applied_time", SortOrder.ASC)
			.get()
			.getHits();

		if (hits.getTotalHits() <= 0) return Lists.newArrayList();

		return toList(hits, (hit, join) -> 
				new ByAdminResult(
					hit.getId(),
					(String)join.get("group_id"), 
					(String)join.get("group_name"), 
					(String)join.get("applicant_id"), 
					(String)join.get("applicant_name"), 
					(String)join.get("applicant_type"), 
					dateTime((String)join.get("applied_time"))));
	}


}
