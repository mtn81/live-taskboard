package jp.mts.base.infrastructure.jdbc;

import java.sql.Timestamp;
import java.util.Date;

import jp.mts.base.infrastructure.jdbc.model.EventTrackModel;
import jp.mts.libs.event.mq.MqEventProcessTracker;

public class JdbcMqEventProcessTracker implements MqEventProcessTracker {

	@Override
	public boolean ignoreProcess(
			String category, 
			String id, 
			Date occurred) {
		
		long count = EventTrackModel.count(
				"category=? and key=? and occurred > ?", category, id, new Timestamp(occurred.getTime()));
		return count > 0;
	}

	@Override
	public void endProcess(
			String category, 
			String id, 
			Date occurred) {
		
		EventTrackModel model = EventTrackModel.findFirst(
				"category=? and key=?", category, id);
		if (model == null) {
			model = new EventTrackModel();
		}
		model.set(
				"category", category,
				"key", id)
			.setTimestamp("occurred", occurred)
			.saveIt();
	}

}
