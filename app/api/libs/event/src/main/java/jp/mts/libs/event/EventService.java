package jp.mts.libs.event;

import java.util.List;
import java.util.UUID;

import jp.mts.libs.event.eventstore.EventStore;
import jp.mts.libs.event.eventstore.StoredEvent;
import jp.mts.libs.event.eventstore.StoredEventSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class EventService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private StoredEventSerializer storedEventSerializer = new StoredEventSerializer();
	private RabbitTemplate rabbitTemplate;
	private EventStore eventStore;
	private EventProcessRecord eventProcessRecord;
	
	public EventService(
			RabbitTemplate rabbitTemplate,
			EventStore eventStore, 
			EventProcessRecord eventProcessRecord) {
		this.rabbitTemplate = rabbitTemplate;
		this.eventStore = eventStore;
		this.eventProcessRecord = eventProcessRecord;
	}

	public void delegate(Event event) {
		StoredEvent storedEvent = storedEventSerializer.serialize(event);
		if (event.eventDelegateType() == EventDelegateType.EVENT_STORE) {
			eventStore.add(storedEvent);
		}else if (event.eventDelegateType() == EventDelegateType.DIRECT) {
			sendEvent(storedEvent);
		}
	}
	
	public void sendEventsInStore() {
		
		List<StoredEvent> targetEvents = eventStore.findEventsAfter(
				eventProcessRecord.lastDelegatedEventId());

		targetEvents.forEach(this::sendEvent);
	}
	
	
	private void sendEvent(StoredEvent e) {
		MessageProperties props = MessagePropertiesBuilder.newInstance()
			.setContentType(MessageProperties.CONTENT_TYPE_BYTES)
			.setMessageId(UUID.randomUUID().toString())
			.setHeader("eventId", e.getEventId())
			.setHeader("occurred", e.getOccurred())
			.setHeader("eventType", e.getEventType())
			.build();
		Message message = MessageBuilder
			.withBody(e.getEventBody())
			.andProperties(props)
			.build();

		rabbitTemplate.send(message);
		logger.debug("send event: " + e.getEventId());
		
		eventProcessRecord.delegate(e);
	}
}
