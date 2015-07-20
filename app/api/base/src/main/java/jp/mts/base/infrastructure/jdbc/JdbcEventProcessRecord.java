package jp.mts.base.infrastructure.jdbc;

import jp.mts.base.infrastructure.jdbc.model.EventProcessModel;
import jp.mts.libs.event.EventProcessRecord;
import jp.mts.libs.event.eventstore.StoredEvent;

public class JdbcEventProcessRecord implements EventProcessRecord {

	@Override
	public long lastDelegatedEventId() {
		EventProcessModel model = EventProcessModel.findFirst("type=?", "delegate");
		if (model == null){
			throw new IllegalStateException();
		}
		return model.getLong("last_event_id");
	}

	@Override
	public void delegate(StoredEvent e) {
		EventProcessModel model = EventProcessModel.findFirst("type=?", "delegate");
		if (model == null){
			throw new IllegalStateException();
		}
		model.set("last_event_id", e.getEventId()).saveIt();
	}

}
