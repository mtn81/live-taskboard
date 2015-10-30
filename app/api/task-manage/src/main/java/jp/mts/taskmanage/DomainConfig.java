package jp.mts.taskmanage;

import javax.annotation.PostConstruct;

import jp.mts.taskmanage.domain.model.auth.MemberAuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class DomainConfig {
	
	@Autowired
	private Settings settings;
	
	@PostConstruct
	public void initDomain() {
		MemberAuth.setExpirationMinutes(settings.getMemberAuthExpirationMinutes());
	}
	
	@Component
	@ConfigurationProperties("domain")
	public static class Settings {
		private int memberAuthExpirationMinutes;

		public int getMemberAuthExpirationMinutes() {
			return memberAuthExpirationMinutes;
		}
		public void setMemberAuthExpirationMinutes(int memberAuthExpirationMinutes) {
			this.memberAuthExpirationMinutes = memberAuthExpirationMinutes;
		}
	}
}
