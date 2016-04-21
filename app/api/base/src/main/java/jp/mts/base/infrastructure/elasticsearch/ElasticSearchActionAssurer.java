package jp.mts.base.infrastructure.elasticsearch;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;

import jp.mts.base.util.DateUtils;
import jp.mts.base.util.ListUtils;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;

import com.google.common.collect.Lists;

public class ElasticSearchActionAssurer {
	
	private TransportClient transportClient;
	private int retryMaxCount;
	private String index;
	private String type;
	
	public ElasticSearchActionAssurer(
			String index,
			int retryMaxCount,
			TransportClient transportClient) {
		super();
		this.transportClient = transportClient;
		this.retryMaxCount = retryMaxCount;
		this.index = index;
		this.type = "action_tracking";
	}

	public void ensureAction(String trackingId, Action action) {

		IndexResponse tracking = transportClient.prepareIndex(index, type)
			.setSource(
				"tracking_id", trackingId,
				"time", dateTime(new Date()))
			.get();

		List<Exception> exceptions = Lists.newArrayList();
		boolean error = true;
		for(int i = 0; i < retryMaxCount; i++) {
			try {
				action.execute();
				error = false;
				break;
			} catch(ElasticsearchException e) {
				exceptions.add(e);
			}
		}
		
		if (error) {
			transportClient.prepareUpdate(index, type, tracking.getId())
				.setDoc("errors", ListUtils.convert(exceptions, e -> {
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					PrintStream printStream = new PrintStream(outputStream);
					e.printStackTrace(printStream);
					return new String(outputStream.toByteArray());
				}))
				.get();
		} else {
			transportClient.prepareDelete(index, type, tracking.getId()).get();
		}
	
	}
	
	@FunctionalInterface
	public interface Action {
		public void execute();
	}
	
	
	protected String dateTime(Date date) {
		return DateUtils.format(date, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	}
}
