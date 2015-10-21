package jp.mts.libs.event.mq;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.eventstore.StoredEventSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class EventMqListener {

	private static Logger logger = LoggerFactory.getLogger(EventMqListener.class);
	
	@Autowired
	private StoredEventSerializer storedEventSerializer;
	
	
	protected void processTemplate(Message message, ProcessBody process) {
		
		Map<String, Object> headers = message.getMessageProperties().getHeaders();
		long eventId = (Long)headers.get("eventId");
		Date occurred = (Date)headers.get("occurred");
		String eventType = (String)headers.get("eventType");
		EventBody eventBody = storedEventSerializer.deserializeBody(message.getBody());
	
		if (willProcessEvent(eventType)) {
			logger.debug("event proccess: eventId={}, eventType={}", eventId, eventType);
			process.doProccess(eventId, occurred, eventBody);
		}
	}
	
	@FunctionalInterface
	protected interface ProcessBody {
		
		abstract void doProccess(
			long eventId, Date occurred, EventBody eventBody);
	}
	
	protected boolean willProcessEvent(String eventType) {
		EventMqListenerConfig config = this.getClass().getAnnotation(EventMqListenerConfig.class);
		if (config == null) {
			throw new IllegalStateException();
		}
		logger.debug("event proccess check: incommingEvent={}, targetEventType={}", eventType, config.targetEventTypes());

		return Arrays.asList(config.targetEventTypes()).contains(eventType);
	}
	
}
