package jp.mts.libs.cache;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.lambdaworks.redis.LettuceFutures;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisFuture;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;

public class RedisCacheMap<K, V> {
	
	private String prefix;
	private Encoder<String, K> keyEncoder;
	private Encoder<Map<String, String>, V> valueEncoder;
	private RedisClient redisClient;
	
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
		this.redisClient = redisClient;
	}

	public void put(K key, V value) {
		if (key == null || value == null) 
			throw new IllegalArgumentException();

		Map<String, String> encodedValue = valueEncoder.encode(value);
		if (encodedValue.isEmpty()) 
			throw new IllegalArgumentException();

		doSync(conn -> {
			conn.sync().hmset(
				key(key), 
				valueEncoder.encode(value));
		});
	}

	public Optional<V> get(K key) {
		return doSyncResult(conn -> {
			Map<String, String> value = conn.sync().hgetall(key(key));
			return (value == null || value.isEmpty()) ? Optional.empty() : Optional.of(valueEncoder.decode(value));
		});
	}
	public List<V> get(List<K> keys) {
		
		return doAsyncResult(
			commands -> {
				return keys.stream()
						.map(this::key)
						.map(key -> commands.hgetall(key))
						.collect(Collectors.toList());
			}, 
			futures -> {
				return futures.stream()
					.<V>map(f -> {
						try {
							Map<String, String> value = (Map<String, String>)f.get();
							if(value == null || value.size() <= 0){
								return null;
							} else {
								return valueEncoder.decode(value);
							}
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					})
					.filter(v -> v != null)
					.collect(Collectors.toList());
			});
	}
	
	public void remove(K key) {
		doSync(conn -> {
			conn.sync().del(key(key));
		});
	}
	
	private String key(K key) {
		String encodedKey = keyEncoder.encode(key);
		return StringUtils.isEmpty(prefix) ? encodedKey : (prefix + ":" + encodedKey);
	}
	
	private <R> R doSyncResult(RedisCommand<R> command) {
		try(StatefulRedisConnection<String, String> connection = this.redisClient.connect()) {
			return command.execute(connection);
		}
	}
	private void doSync(RedisVoidCommand command) {
		try(StatefulRedisConnection<String, String> connection = this.redisClient.connect()) {
			command.execute(connection);
		}
	}

	private <R> R doAsyncResult(
			RedisAsyncCommandBuilder commandBuilder,
			RedisAsyncCommandConsumer<R> commandConsumer) {
		try(RedisAsyncCommands<String, String> connection = this.redisClient.connect().async()) {
			connection.setAutoFlushCommands(false);
			
			List<RedisFuture<?>> futures = commandBuilder.build(connection);

			connection.flushCommands();

			LettuceFutures.awaitAll(
					5, TimeUnit.SECONDS, 
					futures.toArray(new RedisFuture[futures.size()]));

			return commandConsumer.consume(futures);
		}
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

	@FunctionalInterface
	private interface RedisAsyncCommandBuilder {
		List<RedisFuture<?>> build(RedisAsyncCommands<String, String> commands);
	}
	@FunctionalInterface
	private interface RedisAsyncCommandConsumer<R> {
		R consume(List<RedisFuture<?>> futures);
	}
	@FunctionalInterface
	private interface RedisCommand<R> {
		R execute(StatefulRedisConnection<String, String> connection);
	}
	@FunctionalInterface
	private interface RedisVoidCommand {
		void execute(StatefulRedisConnection<String, String> connection);
	}
}
