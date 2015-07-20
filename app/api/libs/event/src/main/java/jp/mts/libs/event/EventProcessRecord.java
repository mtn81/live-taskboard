package jp.mts.libs.event;

import jp.mts.libs.event.eventstore.StoredEvent;

public interface EventProcessRecord {
	
	long lastDelegatedEventId();

	void delegate(StoredEvent e);
}
