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
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.MemberBuilder;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.domain.model.MemberRegisterType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
@MqEventHandlerConfig(targetEventTypes="mts:taskmanage/GroupJoinApplicated")
public class GroupJoinApplicatedEventHandler implements MqEventHandler {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	@Qualifier("groupJoinApplied")
	private MailTemplate mailTemplate;

	@Override
	public void handleEvent(
			long eventId, 
			String publisherId, 
			Date occurred,
			EventBody eventBody) {
		
		String applicantId = eventBody.asString("applicantId");
		String groupId = eventBody.asString("groupId");
		
//TODO
		List<Member> groupAdminMembers = Lists.newArrayList(
				new MemberBuilder(new Member(new MemberId("m01"), "ライブ太郎", MemberRegisterType.GOOGLE)).setEmail("nantsuka2011@gmail.com").get());
		Member applicantMember = new Member(new MemberId("m01"), "ライブ太郎", MemberRegisterType.GOOGLE);
		Group applicatedGroup = new Group(new GroupId("g01"), null, "グループ０１", "");
		

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(toEmails(groupAdminMembers));
		mailMessage.setText(mailTemplate.build(new MailView(applicatedGroup, applicantMember)));
		javaMailSender.send(mailMessage);
	}

	private String[] toEmails(List<Member> members) {
		return members.stream()
				.map(m -> m.email())
				.collect(Collectors.toList())
				.toArray(new String[members.size()]);
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
