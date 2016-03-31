package jp.mts.taskmanage.mq.listener;

import jp.mts.base.lib.mail.MailTemplate;
import jp.mts.libs.event.mq.MqEventHandlerConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@MqEventHandlerConfig(targetEventTypes="mts:taskmanage/GroupJoinAccepted")
public class GroupJoinAcceptedEventHandler extends AbstractGroupJoinHandledEventHandler {

	@Autowired
	public GroupJoinAcceptedEventHandler(
			@Qualifier("groupJoinAccepted") MailTemplate mailTemplate) {
		super(mailTemplate);
	}
}
