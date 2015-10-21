package jp.mts.base.config;

import jp.mts.base.infrastructure.jdbc.JdbcEventProcessRecord;
import jp.mts.base.infrastructure.jdbc.JdbcEventStore;
import jp.mts.base.task.EventDelegateTask;
import jp.mts.libs.event.EventProcessRecord;
import jp.mts.libs.event.EventService;
import jp.mts.libs.event.eventstore.EventStore;
import jp.mts.libs.event.eventstore.StoredEventSerializer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventStoreConfig {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
 
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
	@Bean
	public EventService eventService() {
		return new EventService(rabbitTemplate, eventStore(), eventProcessRecord());
	}
	@Bean
	public EventDelegateTask eventDelegateTask() {
		return new EventDelegateTask(eventService());
	}
	
}
