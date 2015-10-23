package jp.mts.libs.event.mq;

import java.util.Date;

import jp.mts.libs.event.eventstore.EventBody;

@FunctionalInterface
public interface MqEventHandler {

	void handleEvent(
		long eventId, Date occurred, EventBody eventBody);
}
