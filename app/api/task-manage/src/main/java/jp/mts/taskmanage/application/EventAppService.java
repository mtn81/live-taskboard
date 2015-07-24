package jp.mts.taskmanage.application;

import java.util.List;
import java.util.UUID;

import jp.mts.libs.event.EventProcessRecord;
import jp.mts.libs.event.eventstore.EventStore;
import jp.mts.libs.event.eventstore.StoredEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventAppService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private EventStore eventStore;
	@Autowired
	private EventProcessRecord eventProcessRecord;
	
	public void delegateEvent() {
		
		List<StoredEvent> targetEvents = eventStore.findEventsAfter(
				eventProcessRecord.lastDelegatedEventId());

		targetEvents.forEach(e -> {

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
		});
	}
	
}
