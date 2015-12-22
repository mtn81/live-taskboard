package jp.mts.taskmanage.mq.listener;

import java.util.Date;

import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.mq.MqEventHandler;
import jp.mts.libs.event.mq.MqEventHandlerConfig;
import jp.mts.taskmanage.application.MemberAppService;
import jp.mts.taskmanage.domain.model.MemberRegisterType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@MqEventHandlerConfig(targetEventTypes="mts:authaccess/UserEntried")
public class CreateMemberEventHandler implements MqEventHandler {

	@Autowired
	private MemberAppService memberAppService;
	
	@Override
	public void handleEvent(
			long eventId, String publisherId, Date occurred, EventBody eventBody) {
		String memberId = eventBody.asString("userId");
		String name = eventBody.asString("name");
		String userType = eventBody.asString("type");

		memberAppService.registerMember(
				memberId, 
				name,
				MemberRegisterType.valueOf(userType));
	}
}
