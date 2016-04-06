package jp.mts.libs.cache;

import java.util.Map;

import com.google.common.base.Optional;
import com.lambdaworks.redis.api.StatefulRedisConnection;

public class RedisCacheMap<K, V> {
	
	private String prefix;
	private StatefulRedisConnection<String, String> redisConnection;
	private Encoder<String, K> keyEncoder;
	private Encoder<Map<String, String>, V> valueEncoder;
	
	public RedisCacheMap(
			String prefix, 
			StatefulRedisConnection<String, String> redisConnection,
			Encoder<String, K> keyEncoder,
			Encoder<Map<String, String>, V> valueEncoder) {

		this.prefix = prefix;
		this.redisConnection = redisConnection;
		this.keyEncoder = keyEncoder;
		this.valueEncoder = valueEncoder;
	}

	public static interface Encoder<S, T> {
		S encode(T value);
		T decode(S value);
	}

	public void put(K key, V valuye) {
		// TODO Auto-generated method stub
		
	}
	public Optional<V> get(K key) {
		return null;
	}
}
