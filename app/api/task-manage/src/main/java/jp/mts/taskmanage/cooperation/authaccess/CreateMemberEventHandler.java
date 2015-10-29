package jp.mts.taskmanage.cooperation.authaccess;

import java.util.Date;

import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.mq.MqEventHandler;
import jp.mts.libs.event.mq.MqEventHandlerConfig;
import jp.mts.taskmanage.application.MemberAppService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@MqEventHandlerConfig(targetEventTypes="jp.mts.authaccess.domain.model.UserRegistered")
public class CreateMemberEventHandler implements MqEventHandler {

	@Autowired
	private MemberAppService memberAppService;
	
	@Override
	public void handleEvent(long eventId, Date occurred, EventBody eventBody) {
		String memberId = eventBody.asString("userId.value");
		String name = eventBody.asString("name");
		memberAppService.registerMember(memberId, name);
	}
}
