package jp.mts.base.infrastructure.elasticsearch;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ElasticSearchActionAssurerTest {

	ElasticSearchActionAssurer target;
	@Mocked ElasticSearchActionAssurer.Action mockAction;
	@Mocked TransportClient transportClient;
	
	@Before 
	public void setup() {
		target = new ElasticSearchActionAssurer("test", 5, transportClient);
	}
	
	@Test public void 
	test__retry_on_error() {
		
		new Expectations() {{
			mockAction.execute();
				result = Lists.newArrayList(
						new ElasticsearchException(""), 
						new ElasticsearchException(""), 
						null);
		}};
		
		target.ensureAction("t01", mockAction);
		
		new Verifications() {{
			mockAction.execute();
				times = 3;
		}};
	}
	@Test public void 
	test__endup_error_with_max_retries(@Mocked UpdateRequestBuilder updateRequestBuilder) {
		
		new Expectations() {{
			mockAction.execute();
				result = Lists.newArrayList(
						new ElasticsearchException(""), 
						new ElasticsearchException(""), 
						new ElasticsearchException(""), 
						new ElasticsearchException(""), 
						new ElasticsearchException(""));
				
		
			transportClient.prepareUpdate("test", "action_tracking", anyString);
				result = updateRequestBuilder;
				times = 1;
		}};
		
		try {
			target.ensureAction("t01", mockAction);
		} catch (Exception e) {
		}
		
		new Verifications() {{
			mockAction.execute();
				times = 5;
		}};
	}

	@Test public void 
	test__record_action_tracing(
			@Mocked IndexRequestBuilder indexRequestBuilder,
			@Mocked DeleteRequestBuilder deleteRequestBuilder) {

		new Expectations() {{
			transportClient.prepareIndex("test", "action_tracking");
				result = indexRequestBuilder;
				times = 1;
			transportClient.prepareDelete("test", "action_tracking", anyString);
				result = deleteRequestBuilder;
				times = 1;
		}};
		
		target.ensureAction("t01", mockAction);
	}

}
