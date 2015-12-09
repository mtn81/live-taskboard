package jp.mts.base.infrastructure.jdbc;

import java.util.ArrayList;
import java.util.List;

import jp.mts.base.infrastructure.jdbc.model.EventModel;
import jp.mts.libs.event.eventstore.EventStore;
import jp.mts.libs.event.eventstore.StoredEvent;

public class JdbcEventStore implements EventStore {

	@Override
	public void add(StoredEvent e) {
		EventModel model = new EventModel();
		model
			.setTimestamp(
				"occurred", e.getOccurred())
			.set(
				"publisher", e.getPublisherId(),
				"type", e.getEventType(),
				"body", e.getEventBody());
		model.saveIt();
	}

	@Override
	public List<StoredEvent> findEventsAfter(long eventId) {
		List<StoredEvent> events = new ArrayList<>();
		EventModel.findWith(model -> {
			events.add(new StoredEvent(
					model.getLongId(), 
					model.getString("publisher"),
					model.getDate("occurred"), 
					model.getString("type"), 
					model.getBytes("body")));
		}, "id > ?", eventId);
		return events;
	}

}
