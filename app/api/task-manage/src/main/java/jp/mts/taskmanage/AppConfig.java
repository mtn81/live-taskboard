package jp.mts.taskmanage;

import javax.annotation.PostConstruct;

import jp.mts.taskmanage.application.query.MemberAuthorizationQuery;
import jp.mts.taskmanage.rest.aspect.MemberContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	
	@Autowired
	private MemberAuthorizationQuery memberAuthorizationQuery;

	@PostConstruct
	public void initDomain() {
		MemberContext.setMemberAuthorizationQuery(memberAuthorizationQuery);
	}
	
}
