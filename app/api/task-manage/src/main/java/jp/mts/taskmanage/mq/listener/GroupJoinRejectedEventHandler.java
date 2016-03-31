package jp.mts.taskmanage.mq.listener;

import jp.mts.base.lib.mail.MailTemplate;
import jp.mts.libs.event.mq.MqEventHandlerConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@MqEventHandlerConfig(targetEventTypes="mts:taskmanage/GroupJoinRejected")
public class GroupJoinRejectedEventHandler extends AbstractGroupJoinHandledEventHandler {

	@Autowired
	public GroupJoinRejectedEventHandler(
			@Qualifier("groupJoinRejected") MailTemplate mailTemplate) {
		super(mailTemplate);
	}

}
