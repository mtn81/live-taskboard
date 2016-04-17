package jp.mts.base.infrastructure.elasticsearch;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import jp.mts.base.util.DateUtils;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class AbstractElasticSearchAccessor {
	
	protected TransportClient transportClient;
	
	private String index;
	private String type;

	protected AbstractElasticSearchAccessor(
			String index, String type, TransportClient transportClient) {
		this.index = index;
		this.type = type;
		this.transportClient = transportClient;
	}
	protected AbstractElasticSearchAccessor(String index, TransportClient transportClient) {
		this(index, null, transportClient);
	}
	
	protected GetRequestBuilder prepareGet(String id) {
		return transportClient.prepareGet(index, type, id);
	}
	protected GetRequestBuilder prepareGet(String type, String id) {
		return transportClient.prepareGet(index, type, id);
	}
	protected IndexRequestBuilder prepareIndex(String id) {
		return transportClient.prepareIndex(index, type, id);
	}
	protected DeleteRequestBuilder prepareDelete(String id) {
		return transportClient.prepareDelete(index, type, id);
	}
	protected UpdateRequestBuilder prepareUpdate(String type, String id) {
		return transportClient.prepareUpdate(index, type, id);
	}
	protected SearchRequestBuilder prepareSearch() {
		return transportClient.prepareSearch(index).setTypes(type);
	}
	protected SearchRequestBuilder prepareSearch(String type) {
		return transportClient.prepareSearch(index).setTypes(type);
	}
	protected DeleteRequest deleteRequest(String id) {
		return new DeleteRequest(index, type, id);
	}
	protected DeleteRequest deleteRequest(String type, String id) {
		return new DeleteRequest(index, type, id);
	}
	protected IndexRequest indexRequest() {
		return new IndexRequest(index, type);
	}
	protected IndexRequest indexRequest(String type) {
		return new IndexRequest(index, type);
	}
	protected UpdateRequest updateRequest(String id) {
		return new UpdateRequest(index, type, id);
	}
	protected UpdateRequest updateRequest(String type, String id) {
		return new UpdateRequest(index, type, id);
	}
	
	protected <K, V> Map<K, V> toMap(
			SearchHits hits, 
			Function<SearchHit, K> keyMapper, 
			Function<SearchHit, V> valueMapper){

		if(hits.getTotalHits() <= 0) return Maps.newHashMap();

		return Arrays.stream(hits.getHits())
			.collect(Collectors.toMap(keyMapper, valueMapper));
	}
	protected <T> List<T> toList(
			SearchHits hits, 
			Function<SearchHit, T> mapper){

		if(hits.getTotalHits() <= 0) return Lists.newArrayList();

		return Arrays.stream(hits.getHits())
			.map(mapper::apply)
			.collect(Collectors.toList());
	}
	protected <T> Optional<T> toObject(
			SearchHits hits, 
			Function<SearchHit, T> mapper){

		if(hits.getTotalHits() <= 0) return Optional.empty();

		return Arrays.stream(hits.getHits())
			.map(mapper::apply)
			.findFirst();
	}

	protected Date dateTime(String property) {
		return DateUtils.parse(property, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	}
	protected String dateTime(Date date) {
		return DateUtils.format(date, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	}


}
