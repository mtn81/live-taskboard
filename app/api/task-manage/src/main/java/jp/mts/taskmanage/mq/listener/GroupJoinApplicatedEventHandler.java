package jp.mts.taskmanage.mq.listener;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jp.mts.base.lib.mail.MailTemplate;
import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.mq.MqEventHandler;
import jp.mts.libs.event.mq.MqEventHandlerConfig;
import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.application.MemberAppService;
import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@MqEventHandlerConfig(targetEventTypes="mts:taskmanage/GroupJoinApplicated")
public class GroupJoinApplicatedEventHandler implements MqEventHandler {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	@Qualifier("groupJoinApplied")
	private MailTemplate mailTemplate;
	
	@Autowired
	private MemberAppService memberAppService;
	@Autowired
	private GroupAppService groupAppService;

	@Override
	public void handleEvent(
			long eventId, 
			String publisherId, 
			Date occurred,
			EventBody eventBody) {
		
		String applicantId = eventBody.asString("applicantId");
		String groupId = eventBody.asString("groupId");
		
		List<Member> groupAdminMembers = memberAppService.findAdminMembersInGroup(groupId);
		Member applicantMember = memberAppService.findById(applicantId);
		Group applicatedGroup = groupAppService.findById(groupId);

		List<String> emails = toEmails(groupAdminMembers);
		if (emails.size() == 0) return;

		javaMailSender.send(mailTemplate.createMessage(
				emails, new MailView(applicatedGroup, applicantMember)));
	}

	private List<String> toEmails(List<Member> members) {
		return members.stream()
				.filter(m -> m.emailNotificationEnabled())
				.map(m -> m.email())
				.collect(Collectors.toList());
	}
	
	public static class MailView extends jp.mts.base.lib.mail.MailView {
		private Group group;
		private Member applicantMember;
		
		public MailView(Group group, Member applicantMember) {
			this.group = group;
			this.applicantMember = applicantMember;
		}

		public String getGroup() {
			return group.name();
		}
		public String getApplicant() {
			return applicantMember.name();
		}
	}
}
