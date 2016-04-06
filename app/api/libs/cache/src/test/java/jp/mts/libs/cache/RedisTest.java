package jp.mts.libs.cache;

import org.junit.Test;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;

public class RedisTest {

	@Test public void
	test_string() {
		RedisClient client = new RedisClient("192.168.77.10");
		StatefulRedisConnection<String, String> connection = client.connect();
		
		connection.sync().set("test", "hoge");
		System.out.println(connection.sync().get("test"));
		
		connection.close();
		client.shutdown();
	}
	
}
