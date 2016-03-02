package jp.mts.libs.event.mq;

import java.util.Date;

public interface MqEventProcessTracker {

	default void processSync(
			String category, 
			String id, 
			Date occurred, 
			Object syncObj,
			Processor processor) {
		
		synchronized (syncObj) {
			try {
				if(startProcess(category, id, occurred)) return;
				processor.process();
				endProcess(category, id, occurred, true);
			} catch (Exception e) {
				endProcess(category, id, occurred, false);
				throw e;
			}
		}
	}

	boolean startProcess(String category, String id, Date occurred);
	void endProcess(String category, String id, Date occurred, boolean success);
	
	@FunctionalInterface
	interface Processor {
		void process();
	}
}
