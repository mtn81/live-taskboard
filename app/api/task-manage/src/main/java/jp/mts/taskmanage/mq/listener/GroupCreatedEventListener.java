package jp.mts.taskmanage.mq.listener;

import java.util.Date;

import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.MemberId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@EventListenerConfig(targetEventTypes={"jp.mts.taskmanage.domain.model.GroupCreated"})
public class GroupCreatedEventListener extends EventListener {

	@Autowired
	private GroupAppService groupAppService;
	
	@Override
	protected void doProccess(long eventId, Date occurred, EventBody eventBody) {
		groupAppService.entryMember(
				new GroupId(eventBody.asString("groupId.value")), 
				new MemberId(eventBody.asString("creator.value")));
	}
}
