package jp.mts.taskmanage.mq.listener;

import java.util.Date;

import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.mq.MqEventHandler;
import jp.mts.libs.event.mq.MqEventHandlerConfig;
import jp.mts.taskmanage.application.GroupAppService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@MqEventHandlerConfig(targetEventTypes="jp.mts.taskmanage.domain.model.GroupCreated")
public class GroupCreatedEventHandler implements MqEventHandler {

	@Autowired
	private GroupAppService groupAppService;
	
	@Override
	public void handleEvent(long eventId, Date occurred, EventBody eventBody) {
		groupAppService.entryGroupAsAdministrator(
				eventBody.asString("groupId.value"), 
				eventBody.asString("creator.value"));
		groupAppService.changeGroupToAvailable(
				eventBody.asString("groupId.value"));
	}
	
}
