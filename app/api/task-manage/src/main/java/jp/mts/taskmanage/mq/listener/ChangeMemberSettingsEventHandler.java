package jp.mts.taskmanage.mq.listener;

import static org.mockito.Matchers.booleanThat;

import java.util.Date;

import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.mq.MqEventHandler;
import jp.mts.libs.event.mq.MqEventHandlerConfig;
import jp.mts.libs.event.mq.MqEventProcessTracker;
import jp.mts.taskmanage.application.MemberAppService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@MqEventHandlerConfig(targetEventTypes="mts:authaccess/UserChanged")
public class ChangeMemberSettingsEventHandler implements MqEventHandler {
	
	@Autowired
	private MemberAppService memberAppService;
	@Autowired
	private MqEventProcessTracker mqEventProcessTracker;

	@Override
	public void handleEvent(
			long eventId, 
			String publisherId, 
			Date occurred,
			EventBody eventBody) {
		
		String memberId = eventBody.asString("userId");
		String name = eventBody.asString("name");
		String email = eventBody.asString("email");
		boolean notifyEmail = eventBody.as(Boolean.class, "notifyEmail");

		mqEventProcessTracker.processSync(
				"mts:authaccess/UserChanged", memberId, occurred, this,
		() -> {
			memberAppService.changeMember(
					memberId, 
					name, 
					notifyEmail ? email : null);
		});
		
	}

}
