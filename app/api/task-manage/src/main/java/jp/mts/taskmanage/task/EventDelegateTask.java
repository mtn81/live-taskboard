package jp.mts.taskmanage.task;

import jp.mts.taskmanage.application.EventAppService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class EventDelegateTask {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EventAppService eventAppService;	
	
	@Scheduled(fixedDelay=30000)
	public void delegateEvent() {
		logger.debug("start delegate event");

		eventAppService.delegateEvent();

		logger.debug("end delegate event");
	}
}
