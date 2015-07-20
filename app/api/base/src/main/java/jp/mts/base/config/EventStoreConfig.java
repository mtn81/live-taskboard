package jp.mts.base.config;

import jp.mts.base.infrastructure.jdbc.JdbcEventProcessRecord;
import jp.mts.base.infrastructure.jdbc.JdbcEventStore;
import jp.mts.libs.event.EventProcessRecord;
import jp.mts.libs.event.eventstore.EventStore;
import jp.mts.libs.event.eventstore.StoredEventSerializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventStoreConfig {

	@Bean
	public StoredEventSerializer storedEventSerializer() {
		return new StoredEventSerializer();
	}
	@Bean
	public EventProcessRecord eventProcessRecord() {
		return new JdbcEventProcessRecord();
	}
	@Bean
	public EventStore eventStore() {
		return new JdbcEventStore();
	}

}
