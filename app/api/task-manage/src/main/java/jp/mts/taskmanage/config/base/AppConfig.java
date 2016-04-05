package jp.mts.taskmanage.config.base;

import javax.annotation.PostConstruct;

import jp.mts.base.lib.mail.MailTemplate;
import jp.mts.taskmanage.application.query.MemberAuthorizationQuery;
import jp.mts.taskmanage.rest.aspect.MemberContext;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	
	@Autowired
	private MemberAuthorizationQuery memberAuthorizationQuery;

	@Autowired
	@Qualifier("mail")
	private VelocityEngine velocityEngine;

	@PostConstruct
	public void initDomain() {
		MemberContext.setMemberAuthorizationQuery(memberAuthorizationQuery);
	}
	
	@Bean
	@Qualifier("groupJoinApplied")
	public MailTemplate groupJoinApplicatedMailTemplate() {
		return new MailTemplate("group_join_applied.vm", "参加申請のお知らせ", velocityEngine);
	}
	@Bean
	@Qualifier("groupJoinAccepted")
	public MailTemplate groupJoinAccepteddMailTemplate() {
		return new MailTemplate("group_join_accepted.vm", "参加申請が受け入れられました", velocityEngine);
	}
	@Bean
	@Qualifier("groupJoinRejected")
	public MailTemplate groupJoinRejecteddMailTemplate() {
		return new MailTemplate("group_join_rejected.vm", "参加申請が拒否されした", velocityEngine);
	}
	@Bean
	@Qualifier("taskRegistered")
	public MailTemplate taskRegisteredMailTemplate() {
		return new MailTemplate("task_registered.vm", "タスクが割り当てられました", velocityEngine);
	}
	@Bean
	@Qualifier("taskModified")
	public MailTemplate taskModifiedMailTemplate() {
		return new MailTemplate("task_modified.vm", "タスクが編集されました", velocityEngine);
	}
	
}
