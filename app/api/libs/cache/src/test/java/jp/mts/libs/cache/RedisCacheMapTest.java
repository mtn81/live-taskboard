package jp.mts.libs.cache;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import jp.mts.libs.unittest.Maps;
import mockit.Mocked;

import org.junit.Test;

import com.lambdaworks.redis.api.StatefulRedisConnection;

public class RedisCacheMapTest {
	
	@Mocked StatefulRedisConnection<String, String> connection;

	@Test
	public void test() {
		
		RedisCacheMap<TestKey, TestValue> target = new RedisCacheMap<TestKey, TestValue>(
				"test", connection,
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
		
		target.put(new TestKey("k01"), new TestValue("v01"));
		target.put(new TestKey("k02"), new TestValue("v02"));
		
		assertThat(target.get(new TestKey("k01")).get().value, is("v01"));
		assertThat(target.get(new TestKey("k02")).get().value, is("v02"));
		assertThat(target.get(new TestKey("k03")).isPresent(), is(false));

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
