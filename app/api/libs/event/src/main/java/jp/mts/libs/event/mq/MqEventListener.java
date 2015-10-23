package jp.mts.libs.event.mq;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.eventstore.StoredEventSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class MqEventListener {

	private static Logger logger = LoggerFactory.getLogger(MqEventListener.class);
	
	@Autowired
	private StoredEventSerializer storedEventSerializer;
	
	
	protected void processTemplate(
			Message message, List<MqEventHandler> handlers) {
		
		Map<String, Object> headers = message.getMessageProperties().getHeaders();
		long eventId = (Long)headers.get("eventId");
		Date occurred = (Date)headers.get("occurred");
		String eventType = (String)headers.get("eventType");
		EventBody eventBody = storedEventSerializer.deserializeBody(message.getBody());
	
		handlers.forEach(handler -> {
			if(isEventTypeHandled(eventType, handler)){
				logger.debug("event proccess: eventId={}, eventType={}", eventId, eventType);
				handler.handleEvent(eventId, occurred, eventBody);
			}
		});
	}
	
	private boolean isEventTypeHandled(String eventType, MqEventHandler handler) {
		MqEventHandlerConfig config = handler.getClass().getAnnotation(MqEventHandlerConfig.class) ;
		return Arrays.asList(config.targetEventTypes()).contains(eventType);
	}
	
}
