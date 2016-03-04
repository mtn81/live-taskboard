package jp.mts.libs.event.mq;

import java.util.Date;

public interface MqEventProcessTracker {

	boolean ignoreProcess(String category, String id, Date occurred);
	void endProcess(String category, String id, Date occurred);
}
