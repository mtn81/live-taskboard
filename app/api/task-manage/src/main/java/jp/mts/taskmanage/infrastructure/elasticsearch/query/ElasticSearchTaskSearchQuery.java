package jp.mts.taskmanage.infrastructure.elasticsearch.query;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.constantScoreQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

import java.util.Arrays;
import java.util.List;

import jp.mts.base.infrastructure.elasticsearch.AbstractElasticSearchAccessor;
import jp.mts.base.util.Assertions;
import jp.mts.taskmanage.application.query.TaskSearchQuery;
import jp.mts.taskmanage.domain.model.task.TaskStatus;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchTaskSearchQuery 
	extends AbstractElasticSearchAccessor
	implements TaskSearchQuery {

	@Autowired
	public ElasticSearchTaskSearchQuery(TransportClient transportClient) {
		super("task-manage", "task", transportClient);
	}

	@Override
	public List<SearchResult> search(
			String groupId, 
			String keyword,
			List<String> memberIds) {
		
		Assertions.assertNonNull(groupId);
		
		BoolQueryBuilder condition = boolQuery()
			.must(termQuery("group_id", groupId));

		if (CollectionUtils.isNotEmpty(memberIds)) {
			condition
				.must(termsQuery("assigned", memberIds));
		}
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(keyword)) {
			condition
				.should(matchQuery("name", keyword))
				.should(matchQuery("memo", keyword));
		}
		
		return toList(
			prepareSearch()
				.setQuery(constantScoreQuery(condition))
					.addHighlightedField("memo")
					.get().getHits(),
			(hit, source) -> {
				String highlightMemoStr = null;
				HighlightField highlightMemo = hit.getHighlightFields().get("memo");
				if (highlightMemo != null) {
					highlightMemoStr = Arrays.stream(highlightMemo.fragments())
						.map(f -> f.string())
						.reduce((f1, f2) -> f1 + "..." + f2)
						.get();
				}

				return new SearchResult(
						(String)source.get("task_id"), 
						(String)source.get("name"), 
						date((String)source.get("deadline")), 
						(String)source.get("assigned"), 
						TaskStatus.valueOf((String)source.get("status")), 
						highlightMemoStr);
			}
		);
	}

}
