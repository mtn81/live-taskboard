package jp.mts.libs.event.eventstore;

import java.util.HashMap;

import jp.mts.libs.event.Event;

import org.msgpack.jackson.dataformat.MessagePackFactory;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class StoredEventSerializer {
	
	private ObjectMapper objectMapper;
	
	public StoredEventSerializer(){
		objectMapper = new ObjectMapper(new MessagePackFactory());
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		objectMapper.setVisibilityChecker(
			objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
				.withGetterVisibility(Visibility.NONE)
				.withSetterVisibility(Visibility.NONE)
				.withFieldVisibility(Visibility.ANY));
	}
	
	public StoredEvent serialize(Event event) {
		try {
			byte[] serialized = objectMapper.writeValueAsBytes(event);
			return new StoredEvent(event.publisherId(), event.occurred(), event.eventType(), serialized);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	public EventBody deserializeBody(StoredEvent event) {
		try {
			return new EventBody(objectMapper.readValue(event.getEventBody(), HashMap.class));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public EventBody deserializeBody(byte[] eventBody) {
		try {
			return new EventBody(objectMapper.readValue(eventBody, HashMap.class));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
