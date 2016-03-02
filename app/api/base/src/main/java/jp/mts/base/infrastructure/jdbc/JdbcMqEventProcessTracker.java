package jp.mts.base.infrastructure.jdbc;

import java.util.Date;

import jp.mts.libs.event.mq.MqEventProcessTracker;

public class JdbcMqEventProcessTracker implements MqEventProcessTracker {

	@Override
	public boolean startProcess(String category, String id, Date occurred) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void endProcess(String category, String id, Date occurred,
			boolean success) {
		// TODO Auto-generated method stub
		
	}

}
