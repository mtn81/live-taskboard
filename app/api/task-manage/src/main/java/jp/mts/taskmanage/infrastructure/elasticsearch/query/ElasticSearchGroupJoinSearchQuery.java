package jp.mts.taskmanage.infrastructure.elasticsearch.query;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.mts.base.infrastructure.elasticsearch.AbstractElasticSearchAccessor;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationStatus;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.HasParentQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.support.QueryInnerHitBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchGroupJoinSearchQuery 
	extends AbstractElasticSearchAccessor
	implements GroupJoinSearchQuery {

	public ElasticSearchGroupJoinSearchQuery(TransportClient transportClient) {
		super("task-manage", "group_join", transportClient);
	}

	@Override
	public List<ByApplicantResult> byApplicant(String memberId) {
		
		SearchHits hits = prepareSearch()
			.setQuery(constantScoreQuery(
				boolQuery()
					.must(termQuery("applicant_id", memberId))
					.mustNot(termQuery("status", "CANCELLED"))
					.should(hasParentQuery("v_group_owner", matchAllQuery())
						.innerHit(new QueryInnerHitBuilder()))
			))
			.addSort("applied_time", SortOrder.ASC)
			.get()
			.getHits();
		
		if (hits.getTotalHits() <= 0) return new ArrayList<>();

		return toList(hits, hit -> {
			Map<String, Object> groupOwner 
				= toObject(hit.getInnerHits().get("v_group_owner"), h -> h.getSource()).get();
			Map<String, Object> join = hit.getSource();
			return new ByApplicantResult(
					hit.getId(),
					(String)join.get("group_id"), 
					(String)groupOwner.get("group_name"), 
					(String)groupOwner.get("member_name"), 
					(String)groupOwner.get("member_type"), 
					dateTime((String)join.get("applied_time")), 
					GroupJoinApplicationStatus.valueOf((String)join.get("status")));
		});
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
