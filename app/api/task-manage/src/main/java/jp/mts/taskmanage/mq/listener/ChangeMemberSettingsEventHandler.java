package jp.mts.taskmanage.mq.listener;

import static org.mockito.Matchers.booleanThat;

import java.util.Date;

import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.mq.MqEventHandler;
import jp.mts.libs.event.mq.MqEventHandlerConfig;

import org.springframework.stereotype.Component;

@Component
@MqEventHandlerConfig(targetEventTypes="mts:authaccess/UserChanged")
public class ChangeMemberSettingsEventHandler implements MqEventHandler {

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
		
		//TODO
	}

}
