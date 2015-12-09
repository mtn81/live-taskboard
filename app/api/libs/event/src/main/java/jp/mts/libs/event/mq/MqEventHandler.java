package jp.mts.libs.event.mq;

import java.util.Date;

import jp.mts.libs.event.eventstore.EventBody;

@FunctionalInterface
public interface MqEventHandler {

	void handleEvent(
		long eventId, 
		String publisherId,
		Date occurred, 
		EventBody eventBody);
}
