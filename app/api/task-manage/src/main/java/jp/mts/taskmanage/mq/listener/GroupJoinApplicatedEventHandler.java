package jp.mts.taskmanage.mq.listener;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jp.mts.base.lib.mail.MailTemplate;
import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.mq.MqEventHandler;
import jp.mts.libs.event.mq.MqEventHandlerConfig;
import jp.mts.libs.unittest.Maps;
import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@MqEventHandlerConfig(targetEventTypes="mts:taskmanage/GroupJoinApplicated")
public class GroupJoinApplicatedEventHandler implements MqEventHandler {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	@Qualifier("GroupJoinApplied")
	private MailTemplate mailTemplate;

	@Override
	public void handleEvent(
			long eventId, 
			String publisherId, 
			Date occurred,
			EventBody eventBody) {
		
		String applicantId = eventBody.asString("applicantId");
		String groupId = eventBody.asString("groupId");
		
		List<Member> groupAdminMembers = null;
		Member applicantMember = null;
		Group applicatedGroup = null;
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(toEmails(groupAdminMembers));
		mailMessage.setText(mailTemplate.build(new Maps()
			.e("applicant", applicantMember.name())
			.e("group", applicatedGroup.name())
			.get()));

		javaMailSender.send(mailMessage);
	}

	private String[] toEmails(List<Member> members) {
		return members.stream()
				.map(m -> m.email())
				.collect(Collectors.toList())
				.toArray(new String[members.size()]);
	}

}
