package jp.mts.taskmanage.config.base;

import java.net.InetAddress;
import java.util.List;

import javax.annotation.PreDestroy;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Configuration
public class DataSourceConfig {
	
	@Autowired
	private Settings settings;
	
	@Bean
	public TransportClient transportClient() throws Exception {
		TransportClient client = TransportClient.builder().build();
		for (String endPoint : settings.transportEndPoints) {
			String[] epParts = endPoint.split("\\:");
			client.addTransportAddress(new InetSocketTransportAddress(
					InetAddress.getByName(epParts[0]), Integer.parseInt(epParts[1])));
		}
		return client;
	}

	@Component
	@ConfigurationProperties("elasticsearch")
	public static class Settings {
		private List<String> transportEndPoints;

		public List<String> getTransportEndPoints() {
			return transportEndPoints;
		}
		public void setTransportEndPoints(List<String> transportEndPoints) {
			this.transportEndPoints = transportEndPoints;
		}
	}
}
