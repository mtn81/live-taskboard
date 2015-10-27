package jp.mts.libs.event.mq;

import java.util.ArrayList;
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

public class MqEventListener {

	private static Logger logger = LoggerFactory.getLogger(MqEventListener.class);
	
	private StoredEventSerializer storedEventSerializer = new StoredEventSerializer();
	
	private List<MqEventHandler> handlers = new ArrayList<>();
	
	public MqEventListener(MqEventHandler... handlers) {
		this.handlers = Arrays.asList(handlers);
	}

	public void process(Message message) {
		
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
	
	public void addHandlers(List<MqEventHandler> handlers) {
		this.handlers.addAll(handlers);
	}
	
	private boolean isEventTypeHandled(String eventType, MqEventHandler handler) {
		MqEventHandlerConfig config = handler.getClass().getAnnotation(MqEventHandlerConfig.class) ;
		return Arrays.asList(config.targetEventTypes()).contains(eventType);
	}
	
}