package jp.mts.taskmanage.infrastructure.elasticsearch.query;

import static org.elasticsearch.index.query.QueryBuilders.constantScoreQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.List;
import java.util.Map;

import jp.mts.base.infrastructure.elasticsearch.AbstractElasticSearchAccessor;
import jp.mts.taskmanage.application.query.GroupBelongingSearchQuery;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchGroupBelongingSearchQuery 
	extends AbstractElasticSearchAccessor
	implements GroupBelongingSearchQuery {
	
	@Autowired
	public ElasticSearchGroupBelongingSearchQuery(TransportClient transportClient) {
		super("task-manage", "view_group_belonging", transportClient);
	}

	@Override
	public List<GroupSummary> byMember(String memberId) {
		
		SearchResponse searchResponse = prepareSearch()
			.setQuery(constantScoreQuery(
				termQuery("member_id", memberId)
			))
			.addSort("group_id", SortOrder.ASC)
			.get();
		
		return toList(searchResponse.getHits(), hit -> {
			Map<String, Object> groupBelonging = hit.getSource();
			
			return new GroupSummary(
					(String)groupBelonging.get("group_id"), 
					(String)groupBelonging.get("group_name"), 
					(boolean)groupBelonging.get("admin"));
		});

	}
}
