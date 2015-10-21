package jp.mts.base.task;

import jp.mts.libs.event.EventService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class EventDelegateTask {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private EventService eventService;	
	
	public EventDelegateTask(EventService eventService) {
		this.eventService = eventService;
	}

	@Scheduled(fixedDelay=3000)
	public void delegateEvent() {
		logger.debug("start delegate event");
		eventService.delegateEvent();
		logger.debug("end delegate event");
	}
}
