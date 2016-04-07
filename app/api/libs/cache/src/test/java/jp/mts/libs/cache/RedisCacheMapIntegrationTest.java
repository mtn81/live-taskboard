package jp.mts.libs.cache;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import jp.mts.libs.unittest.Maps;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.lambdaworks.redis.RedisClient;

@Ignore
public class RedisCacheMapIntegrationTest {
	
	RedisCacheMap<TestKey, TestValue> target;
	
	@Before
	public void setup() {
		RedisClient redisClient = new RedisClient("192.168.77.10");

		target = new RedisCacheMap<TestKey, TestValue>(
				redisClient, "test", 
				new RedisCacheMap.Encoder<String, TestKey>() {
					@Override
					public String encode(TestKey value) {
						return value.value;
					}
					@Override
					public TestKey decode(String value) {
						return new TestKey(value);
					}
				},
				new RedisCacheMap.Encoder<Map<String, String>, TestValue>() {
					@Override
					public Map<String, String> encode(TestValue value) {
						return Maps.map("name", value.value);
					}
					@Override
					public TestValue decode(Map<String, String> value) {
						return new TestValue(value.get("name"));
					}
				});
	}

	@Test
	public void test() {
		
		target.put(new TestKey("k01"), new TestValue("v01"));
		target.put(new TestKey("k02"), new TestValue("v02"));
		target.put(new TestKey("k03"), new TestValue("v03"));
		
		target.remove(new TestKey("k03"));

		assertThat(target.get(new TestKey("k01")).get().value, is("v01"));
		assertThat(target.get(new TestKey("k02")).get().value, is("v02"));
		assertThat(target.get(new TestKey("k03")).isPresent(), is(false));
		assertThat(target.get(new TestKey("k04")).isPresent(), is(false));

	}
	
	public static class TestKey {
		public String value;

		public TestKey(String value) {
			this.value = value;
		}
	}
	public static class TestValue {
		public String value;
		
		public TestValue(String value) {
			this.value = value;
		}
	}

}
