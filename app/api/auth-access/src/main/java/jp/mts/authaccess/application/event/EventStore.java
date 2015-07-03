package jp.mts.authaccess.application.event;

import java.util.List;

public interface EventStore {
	
	void add(StoredEvent e);
	List<StoredEvent> findEventsAfter(long eventId);
}
