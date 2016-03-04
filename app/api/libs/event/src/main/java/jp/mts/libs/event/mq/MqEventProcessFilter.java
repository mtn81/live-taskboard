package jp.mts.libs.event.mq;

import java.util.Date;

public class MqEventProcessFilter {
	
	private MqEventProcessTracker mqEventProcessTracker;
	
	public MqEventProcessFilter(
			MqEventProcessTracker mqEventProcessTracker) {
		this.mqEventProcessTracker = mqEventProcessTracker;
	}

	public void processIfNewer(
			String category, 
			String id, 
			Date occurred, 
			Object syncObj,
			Processor processor) {
		
		synchronized (syncObj) {
			try {
				if(mqEventProcessTracker.ignoreProcess(category, id, occurred)) return;
				processor.process();
				mqEventProcessTracker.endProcess(category, id, occurred);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	
	@FunctionalInterface
	public static interface Processor {
		void process();
	}
	
}
