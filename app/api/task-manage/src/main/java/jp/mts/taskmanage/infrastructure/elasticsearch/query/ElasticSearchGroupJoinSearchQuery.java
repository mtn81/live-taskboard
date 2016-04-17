package jp.mts.taskmanage.infrastructure.elasticsearch.query;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jp.mts.base.infrastructure.elasticsearch.AbstractElasticSearchAccessor;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationStatus;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.HasParentQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.support.QueryInnerHitBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class ElasticSearchGroupJoinSearchQuery 
	extends AbstractElasticSearchAccessor
	implements GroupJoinSearchQuery {

	public ElasticSearchGroupJoinSearchQuery(TransportClient transportClient) {
		super("task-manage", "group_join", transportClient);
	}

	@Override
	public List<ByApplicantResult> byApplicant(String memberId) {
		
		SearchHits hits = prepareSearch("group")
			.setQuery(constantScoreQuery(
				boolQuery()
					.must(hasChildQuery("group_join", constantScoreQuery(
						boolQuery()
							.must(termQuery("applicant_id", memberId))
							.mustNot(termQuery("status", "CANCELLED"))
					)).innerHit(new QueryInnerHitBuilder()))
					.should(hasChildQuery("v_group_owner", constantScoreQuery(
						matchAllQuery())
					).innerHit(new QueryInnerHitBuilder()))
			))
			.get()
			.getHits();

		if (hits.getTotalHits() <= 0) return Lists.newArrayList();

		return Arrays.stream(hits.getHits())
			.<ByApplicantResult>flatMap(ghit -> {
				Map<String, Object> group = ghit.getSource();
				Map<String, Object> groupOwner = toObject(ghit.getInnerHits().get("v_group_owner"), h -> h.getSource()).get();
				return Arrays.stream(ghit.getInnerHits().get("group_join").getHits()).map(jhit -> {
					Map<String, Object> join = jhit.getSource();
					return new ByApplicantResult(
							jhit.getId(),
							(String)join.get("group_id"), 
							(String)group.get("name"), 
							(String)groupOwner.get("member_name"), 
							(String)groupOwner.get("member_type"), 
							dateTime((String)join.get("applied_time")), 
							GroupJoinApplicationStatus.valueOf((String)join.get("status")));
				});
			})
			.sorted((j1, j2) -> j1.joinApplied.compareTo(j2.joinApplied))
			.collect(Collectors.toList());
	}

	@Override
	public List<ByAdminResult> acceptableByAdmin(String memberId) {
		return null;
	}

	@Override
	public List<ByAdminResult> rejectedByAdmin(String memberId) {
		return null;
	}

	@Override
	public List<NotJoinAppliedWithNameResult> notJoinAppliedWithName(
			String memberId, String groupName) {
		return null;
	}

}
