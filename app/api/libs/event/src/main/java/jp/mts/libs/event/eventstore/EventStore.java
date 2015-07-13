package jp.mts.libs.event.eventstore;

import java.util.List;

public interface EventStore {
	
	void add(StoredEvent e);
	List<StoredEvent> findEventsAfter(long eventId);
}
