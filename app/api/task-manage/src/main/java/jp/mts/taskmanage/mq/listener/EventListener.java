package jp.mts.taskmanage.mq.listener;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.eventstore.StoredEventSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class EventListener {

	private static Logger logger = LoggerFactory.getLogger(EventListener.class);
	
	@Autowired
	private StoredEventSerializer storedEventSerializer;
	
	//@RabbitListener(queues="task-manage")
	public void proccess(Message message) {
		Map<String, Object> headers = message.getMessageProperties().getHeaders();
		long eventId = (Long)headers.get("eventId");
		Date occurred = (Date)headers.get("occurred");
		String eventType = (String)headers.get("eventType");
		EventBody eventBody = storedEventSerializer.deserializeBody(message.getBody());
	
		if (willProcessEvent(eventType)) {
			logger.debug("event proccess: eventId={}, eventType={}", eventId, eventType);
			doProccess(eventId, occurred, eventBody);
		}
	}
	
	protected boolean willProcessEvent(String eventType) {
		EventListenerConfig config = this.getClass().getAnnotation(EventListenerConfig.class);
		if (config == null) {
			throw new IllegalStateException();
		}
		logger.debug("event proccess check: incommingEvent={}, targetEventType={}", eventType, config.targetEventTypes());

		return Arrays.asList(config.targetEventTypes()).contains(eventType);
	}
	
	protected abstract void doProccess(
			long eventId, Date occurred, EventBody eventBody);
}
