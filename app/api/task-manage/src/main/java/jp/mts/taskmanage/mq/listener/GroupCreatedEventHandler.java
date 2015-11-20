package jp.mts.taskmanage.mq.listener;

import java.util.Date;

import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.mq.MqEventHandler;
import jp.mts.libs.event.mq.MqEventHandlerConfig;
import jp.mts.taskmanage.application.GroupAppService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@MqEventHandlerConfig(targetEventTypes="jp.mts.authaccess.domain.model.GroupCreated")
public class GroupCreatedEventHandler implements MqEventHandler {

	@Autowired
	private GroupAppService groupAppService;
	
	@Override
	public void handleEvent(long eventId, Date occurred, EventBody eventBody) {
		String memberId = eventBody.asString("memberId.value");
		String groupId = eventBody.asString("groupId.value");
		
		groupAppService.entryGroup(groupId, memberId, true);
		
	}
}
