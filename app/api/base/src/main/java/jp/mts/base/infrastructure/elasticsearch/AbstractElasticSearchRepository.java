package jp.mts.base.infrastructure.elasticsearch;

import static org.elasticsearch.index.query.QueryBuilders.constantScoreQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import jp.mts.base.domain.model.DomainEntity;
import jp.mts.base.util.DateUtils;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;

public class AbstractElasticSearchRepository extends AbstractElasticSearchAccessor {
	
	protected AbstractElasticSearchRepository(
			String index, String type, TransportClient transportClient) {
		super(index, type, transportClient);
	}
	
	protected <E extends DomainEntity<?>> Optional<E> searchDomainById(
			String id, Function<Map<String, Object>, E> mapper) {
		SearchHits hits = prepareSearch()
				.setQuery(constantScoreQuery(termQuery("_id", id))).get().getHits();
		if (hits.getTotalHits() <= 0) return Optional.empty();
		return Optional.of(mapper.apply(hits.getAt(0).getSource()));
	}
	protected <E extends DomainEntity<?>> Optional<E> getDomain(
			String id, Function<Map<String, Object>, E> mapper) {
		GetResponse getResponse = prepareGet(id).get();
		Map<String, Object> source = getResponse.getSourceAsMap();
		if (source == null) return Optional.empty();
		return Optional.of(mapper.apply(source));
	}
	protected void delete(String id) {
		prepareDelete(id).get();
	}
	protected <E extends DomainEntity<?>> void save(
			String id,
			E entity, 
			Function<E, Map<String, Object>> mapper) {
		prepareIndex(id)
			.setSource(mapper.apply(entity))
			.get();
	}
	protected <E extends DomainEntity<?>> void save(
			String id,
			E entity, 
			String parent,
			Function<E, Map<String, Object>> mapper) {
		prepareIndex(id)
			.setSource(mapper.apply(entity))
			.setParent(parent)
			.get();
	}
	protected void await() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
	
}
