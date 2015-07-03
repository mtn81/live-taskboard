package jp.mts.authaccess.application.event;

import java.util.HashMap;

import jp.mts.authaccess.domain.model.DomainEvent;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
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
	
	public StoredEvent serialize(DomainEvent event) {
		try {
			byte[] serialized = objectMapper.writeValueAsBytes(event);
			return new StoredEvent(event.occurred(), event.eventType(), serialized);
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
}
