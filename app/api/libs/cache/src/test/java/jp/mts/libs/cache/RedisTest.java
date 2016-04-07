package jp.mts.libs.cache;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jp.mts.libs.unittest.Maps;

import org.junit.Ignore;
import org.junit.Test;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

@Ignore
public class RedisTest {

	@Test public void
	test() {
		RedisClient client = new RedisClient("192.168.77.10");
		StatefulRedisConnection<String, String> connection = client.connect();
		
		RedisCommands<String, String> syncCommands = connection.sync();

		syncCommands.set("test", "hoge");
		assertThat(syncCommands.get("test"), is("hoge"));
		
		syncCommands.hmset("maptest", new Maps().e("name", "taro").e("age", "20").get());
		assertThat(syncCommands.hgetall("maptest"), is(new Maps().e("name", "taro").e("age", "20").get()));
		
		connection.close();
		client.shutdown();
	}
	
}
