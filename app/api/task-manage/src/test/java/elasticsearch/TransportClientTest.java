package elasticsearch;

import static jp.mts.base.util.MapUtils.pairs;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.hasChildQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import jp.mts.base.util.MapUtils;
import jp.mts.libs.unittest.Dates;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.support.QueryInnerHitBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

import com.google.common.collect.Lists;

public class TransportClientTest {
	
	@Test public void 
	test() throws UnknownHostException {
		TransportClient client = TransportClient.builder().build()
			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.77.10"), 9300));
		
		IndexResponse indexResponse = client.prepareIndex("test", "person", "p01")
			.setSource(MapUtils.pairs(
				"name", "taro",
				"age", 20,
				"birthday", Dates.date("2016/01/02"),
				"deadline", Dates.dateTime("2016/01/02 12:59:59.999"),
				"tags", Lists.newArrayList(
						MapUtils.pairs("name", "hoge"),
						MapUtils.pairs("name", "foo"))
			))
			.get();
		System.out.println(indexResponse);
		
		UpdateResponse updateResponse = client.prepareUpdate("test", "person", "p01")
			.setDoc(MapUtils.pairs(
					"age", 18))
			.get();
		System.out.println(updateResponse);
		
		GetResponse getResponse = client.prepareGet("test", "person", "p01").get();
		System.out.println(getResponse.getSourceAsString());
		
		GetResponse getResponse2 = client.prepareGet("test", "person", "p01").setFetchSource("age", null).get();
		System.out.println(getResponse2.getSourceAsString());
		
		GetResponse notExists = client.prepareGet("test", "notexists", "id01").get();
		System.out.println(notExists.getSourceAsMap());
		
		SearchResponse searchResponse = client.prepareSearch("test").setTypes("person")
				.setQuery(QueryBuilders.termQuery("tags.name", "hoge2")).get();
		searchResponse.getHits().forEach(hit -> System.out.println("!!" + hit.getSource()));;

		client.admin().indices().prepareDelete("test").get();
		
		client.close();
	}
	

	@Test public void 
	test_parent_child() throws UnknownHostException, InterruptedException {
		TransportClient client = TransportClient.builder().build()
			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.77.10"), 9300));
		
			client.admin().indices().prepareDelete("test").get();
			client.admin().indices().prepareCreate("test")
				.addMapping("parent", new HashMap<>())
				.addMapping("child", pairs("_parent", pairs("type", "parent")))
				.get();
		
		client.prepareIndex("test", "parent", "p01")
			.setSource(pairs("name", "taro")).get();
		
		client.prepareIndex("test", "parent", "p02")
			.setSource(pairs("name", "jiro")).get();
		
		client.prepareIndex("test", "child", "c01")
			.setSource(pairs("name", "hoge", "age", 20))
			.setParent("p01").get();
		client.prepareIndex("test", "child", "c02")
			.setSource(pairs("name", "foo", "age", 30))
			.setParent("p01").get();
		
		Thread.sleep(1000);
		
		SearchResponse searchParentResponse = client.prepareSearch("test").setTypes("parent")
			.setQuery(boolQuery()
				.must(QueryBuilders.termQuery("_id", "p01"))
				//.must(matchAllQuery())
				.should(hasChildQuery("child", matchAllQuery()).innerHit(new QueryInnerHitBuilder())))
			.get();
		
		searchParentResponse.getHits().forEach(hit -> {
			System.out.println(hit.getSource());
			if (hit.getInnerHits() == null) return;
			hit.getInnerHits().forEach((key, ihit) -> {
				ihit.forEach(iihit -> System.out.println(iihit.getSource()));
			});
		});

		SearchResponse searchChildResponse = client.prepareSearch("test").setTypes("child")
			.setQuery(boolQuery()
				.must(QueryBuilders.matchAllQuery())
				.should(QueryBuilders.hasParentQuery("parent", matchAllQuery()).innerHit(new QueryInnerHitBuilder())))
			.addSort("age", SortOrder.DESC)
			.get();
		System.out.println("!!!");
		searchChildResponse.getHits().forEach(hit -> {
			System.out.println(hit.getSource());
			if (hit.getInnerHits() == null) return;
			hit.getInnerHits().forEach((key, ihit) -> {
				ihit.forEach(iihit -> System.out.println(iihit.getSource()));
			});
		});
	}
}
