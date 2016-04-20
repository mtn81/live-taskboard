package elasticsearch;

import static com.google.common.collect.Lists.newArrayList;
import static jp.mts.base.util.MapUtils.pairs;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.hasChildQuery;
import static org.elasticsearch.index.query.QueryBuilders.hasParentQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import jp.mts.base.util.MapUtils;
import jp.mts.libs.unittest.Dates;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.support.QueryInnerHitBuilder;
import org.elasticsearch.search.fetch.innerhits.InnerHitsBuilder;
import org.elasticsearch.search.fetch.innerhits.InnerHitsBuilder.InnerHit;
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
	test_nested() throws UnknownHostException, InterruptedException, ExecutionException {
		TransportClient client = TransportClient.builder().build()
			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.77.10"), 9300));
		
		if(client.admin().indices().exists(new IndicesExistsRequest("test")).get().isExists()){
			client.admin().indices().prepareDelete("test").get();
		}
		client.admin().indices().prepareCreate("test")
				.addMapping("parent", pairs(
					"properties", pairs(
						"childs", pairs("type", "nested")
					)
				))
				.get();
		
		
		client.prepareIndex("test", "parent", "p01")
			.setSource(pairs(
				"name", "taro",
				"childs", newArrayList(
						pairs("name", "c01", "age", 10),
						pairs("name", "c02", "age", 15))
			)).get();
		client.prepareIndex("test", "parent", "p02")
			.setSource(pairs(
				"name", "jiro",
				"childs", newArrayList(
						pairs("name", "c03", "age", 20),
						pairs("name", "c04", "age", 25))
			)).get();
		
		Thread.sleep(1000);
		
		client.prepareSearch("test").setTypes("parent")
			//.setQuery(matchAllQuery())
			.setQuery(nestedQuery("childs", boolQuery()
					.must(termQuery("childs.age", 20))
					.must(termQuery("childs.name", "c04"))))
			.get()
			.getHits()
			.forEach(hit -> {
				System.out.println(hit.getSource());
			});
	}

	@Test public void 
	test_parent_child() throws UnknownHostException, InterruptedException, ExecutionException {
		TransportClient client = TransportClient.builder().build()
			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.77.10"), 9300));
		
		if(client.admin().indices().exists(new IndicesExistsRequest("test")).get().isExists()){
			client.admin().indices().prepareDelete("test").get();
		}
		client.admin().indices().prepareCreate("test")
				.addMapping("parent", new HashMap<>())
				.addMapping("child", pairs("_parent", pairs("type", "parent")))
				.addMapping("child2", pairs("_parent", pairs("type", "parent")))
				.addMapping("child3", pairs("_parent", pairs("type", "child")))
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

		client.prepareIndex("test", "child2", "cc01")
			.setSource(pairs("name", "bar", "age", 20))
			.setParent("p01").get();
		client.prepareIndex("test", "child2", "cc02")
			.setSource(pairs("name", "baz", "age", 30))
			.setParent("p01").get();

		client.prepareIndex("test", "child3", "ccc01")
			.setSource(pairs("name", "piyo", "age", 20))
			.setParent("c01")
			.setRouting("p01")
			.get();
		
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
		
		System.out.println("!!!!!");
		client.prepareSearch("test")
			.setTypes("parent")
			.setQuery(boolQuery()
				.must(hasChildQuery("child", boolQuery()
						.must(termQuery("age", 20))
						.must(hasChildQuery("child3", matchAllQuery()).innerHit(new QueryInnerHitBuilder()))
					  ).innerHit(new QueryInnerHitBuilder()))
				.should(hasChildQuery("child2", matchAllQuery()).innerHit(new QueryInnerHitBuilder()))
			)
			.get()
			.getHits()
			.forEach(hit -> {
				System.out.println(hit.getSource());
				hit.getInnerHits().get("child").forEach(chit -> {
					System.out.println("c1  " + chit.getSource());
					chit.getInnerHits().get("child3").forEach(c3hit -> {
						System.out.println("c3    " + c3hit.getSource());
					});
				});
				hit.getInnerHits().get("child2").forEach(chit -> {
					System.out.println("c2  " + chit.getSource());
				});
			});
		
		System.out.println("!!!!!");
		client.prepareSearch("test")
			.setTypes("child")
			.setQuery(hasParentQuery("parent",
						hasChildQuery("child2", matchAllQuery()).innerHit(new QueryInnerHitBuilder()))
					.innerHit(new QueryInnerHitBuilder())
			)
			.get()
			.getHits()
			.forEach(chit -> {
				System.out.println(chit.getSource());
				chit.getInnerHits().get("parent").forEach(phit -> {
					System.out.println("p  " + phit.getSource());
//					phit.getInnerHits().get("child2").forEach(c2hit -> {
//						System.out.println("c2  " + c2hit.getSource());
//					});
				});
				chit.getInnerHits().get("child2").forEach(c2hit -> {
					System.out.println("c2  " + c2hit.getSource());
				});
			});
	}
}
