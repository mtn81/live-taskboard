package jp.mts.taskmanage.config.base;

import javax.annotation.PostConstruct;

import jp.mts.base.lib.mail.MailTemplate;
import jp.mts.taskmanage.application.query.MemberAuthorizationQuery;
import jp.mts.taskmanage.rest.aspect.MemberContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	
	@Autowired
	private MemberAuthorizationQuery memberAuthorizationQuery;

	@PostConstruct
	public void initDomain() {
		MemberContext.setMemberAuthorizationQuery(memberAuthorizationQuery);
	}
	
	@Bean
	@Qualifier("GroupJoinApplied")
	public MailTemplate groupJoinApplicatedMailTemplate() {
		return new MailTemplate(""); //TODO
	}
	
}
