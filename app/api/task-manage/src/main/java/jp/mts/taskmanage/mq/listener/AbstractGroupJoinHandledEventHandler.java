package jp.mts.taskmanage.mq.listener;

import java.util.Date;

import jp.mts.base.lib.mail.MailTemplate;
import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.mq.MqEventHandler;
import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.application.MemberAppService;
import jp.mts.taskmanage.domain.model.group.Group;
import jp.mts.taskmanage.domain.model.member.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

abstract class AbstractGroupJoinHandledEventHandler implements MqEventHandler {

	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private GroupAppService groupAppService;
	@Autowired
	private MemberAppService memberAppService;
	
	private MailTemplate mailTemplate;
	
	public AbstractGroupJoinHandledEventHandler(MailTemplate mailTemplate) {
		this.mailTemplate = mailTemplate;
	}

	@Override
	public void handleEvent(
			long eventId, 
			String publisherId, 
			Date occurred, 
			EventBody eventBody) {
		String memberId = eventBody.asString("memberId");
		String groupId = eventBody.asString("groupId");
		
		groupAppService.entryGroup(groupId, memberId, false);
		sendNotificationEmail(memberId, groupId);
	}

	private void sendNotificationEmail(String memberId, String groupId) {
		Group group = groupAppService.findById(groupId);
		Member member = memberAppService.findById(memberId);

		if(!member.emailNotificationEnabled()) return;
		
		javaMailSender.send(mailTemplate.createMessage(
				member.email(), new MailView(group)));
	}
	
	public static class MailView extends jp.mts.base.lib.mail.MailView {
		private Group group;

		public MailView(Group group) {
			this.group = group;
		}
		public String getGroup() {
			return group.name();
		}
	}

}
