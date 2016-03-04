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
				if(ignoreProcess(category, id, occurred)) return;
				processor.process();
				endProcess(category, id, occurred);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	boolean ignoreProcess(String category, String id, Date occurred);
	void endProcess(String category, String id, Date occurred);
	
	@FunctionalInterface
	interface Processor {
		void process();
	}
	
}
