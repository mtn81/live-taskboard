package jp.mts.base.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;

@Configuration
public class CacheConfig {

	@Autowired
	private Settings settings;
	
	@Bean
	public RedisClient redisClient() {
		return RedisClient.create(
				RedisURI.Builder.redis(settings.getHost(), settings.getPort()).build());
	}

	@Component
	@ConfigurationProperties("redis")
	public static class Settings {
		private String host;
		private int port;

		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
	}
}
