package jp.mts.authaccess.infrastructure.jdbc.eventstore;

import java.util.ArrayList;
import java.util.List;

import jp.mts.authaccess.application.event.EventStore;
import jp.mts.authaccess.application.event.StoredEvent;
import jp.mts.authaccess.infrastructure.jdbc.model.EventModel;

import org.springframework.stereotype.Component;

@Component
public class JdbcEventStore implements EventStore {

	@Override
	public void add(StoredEvent e) {
		EventModel model = new EventModel();
		model.set(
			"occurred", new java.sql.Date(e.getOccurred().getTime()),
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
					model.getDate("occurred"), 
					model.getString("type"), 
					model.getBytes("body")));
		}, "id > ?", eventId);
		return events;
	}

}
