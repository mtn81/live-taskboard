package jp.mts.taskmanage.infrastructure.elasticsearch.query;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.List;
import java.util.Map;

import jp.mts.base.infrastructure.elasticsearch.AbstractElasticSearchAccessor;
import jp.mts.taskmanage.application.query.GroupBelongingSearchQuery;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.support.QueryInnerHitBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchGroupBelongingSearchQuery 
	extends AbstractElasticSearchAccessor
	implements GroupBelongingSearchQuery {
	
	@Autowired
	public ElasticSearchGroupBelongingSearchQuery(TransportClient transportClient) {
		super("task-manage", transportClient);
	}

	@Override
	public List<GroupSummary> byMember(String memberId) {
		
		SearchResponse searchResponse = prepareSearch("v_group_belonging")
			.setQuery(constantScoreQuery(
				boolQuery()
					.must(termQuery("member_id", memberId))
					.should(hasParentQuery("group", matchAllQuery()).innerHit(new QueryInnerHitBuilder()))
			))
			.addSort("group_id", SortOrder.ASC)
			.get();
		
		return toList(searchResponse.getHits(), hit -> {
			Map<String, Object> groupBelonging = hit.getSource();
			String groupName = toObject(hit.getInnerHits().get("group"), 
					ihit -> (String)ihit.getSource().get("name")).get();
			
			return new GroupSummary(
					(String)groupBelonging.get("group_id"), 
					groupName, 
					(boolean)groupBelonging.get("admin"));
		});

	}
}
