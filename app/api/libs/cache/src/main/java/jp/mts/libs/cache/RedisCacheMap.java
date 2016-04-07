package jp.mts.libs.cache;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;

public class RedisCacheMap<K, V> {
	
	private StatefulRedisConnection<String, String> redisConnection;
	private String prefix;
	private Encoder<String, K> keyEncoder;
	private Encoder<Map<String, String>, V> valueEncoder;
	
	public static <V> RedisCacheMap<String, V> simpleKeyCache(
			RedisClient redisClient,
			String prefix, 
			Encoder<Map<String, String>, V> valueEncoder) {
		
		return new RedisCacheMap<String, V>(
				redisClient, prefix,
				new IdenticalEncoder<String>(),
				valueEncoder);
	}
	
	public RedisCacheMap(
			RedisClient redisClient,
			String prefix, 
			Encoder<String, K> keyEncoder,
			Encoder<Map<String, String>, V> valueEncoder) {

		this.prefix = prefix;
		this.keyEncoder = keyEncoder;
		this.valueEncoder = valueEncoder;
		this.redisConnection = redisClient.connect();
	}

	public static interface Encoder<S, T> {
		S encode(T value);
		T decode(S value);
	}
	public static class IdenticalEncoder<T> implements Encoder<T, T> {
		@Override
		public T encode(T value) {
			return value;
		}
		@Override
		public T decode(T value) {
			return value;
		}
	}

	public void put(K key, V value) {
		if (key == null || value == null) 
			throw new IllegalArgumentException();

		Map<String, String> encodedValue = valueEncoder.encode(value);
		if (encodedValue.isEmpty()) 
			throw new IllegalArgumentException();

		redisConnection.sync().hmset(
				key(key), 
				valueEncoder.encode(value));
	}

	public Optional<V> get(K key) {
		Map<String, String> value = redisConnection.sync().hgetall(key(key));
		return (value == null || value.isEmpty()) ? Optional.empty() : Optional.of(valueEncoder.decode(value));
	}
	
	public void remove(K key) {
		redisConnection.sync().del(key(key));
	}
	
	private String key(K key) {
		String encodedKey = keyEncoder.encode(key);
		return StringUtils.isEmpty(prefix) ? encodedKey : (prefix + ":" + encodedKey);
	}
}
