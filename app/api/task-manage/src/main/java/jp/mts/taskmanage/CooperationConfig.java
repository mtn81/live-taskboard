package jp.mts.taskmanage;

import jp.mts.taskmanage.infrastructure.cooperation.AuthApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class CooperationConfig {
	
	@Autowired
	private Settings settings;
	
	@Bean
	public AuthApi authApi() {
		return new AuthApi(settings.authAccessUrl);
	}
	
	@Component
	@ConfigurationProperties("cooperation")
	public static class Settings {
		private String authAccessUrl;

		public String getAuthAccessUrl() {
			return authAccessUrl;
		}
		public void setAuthAccessUrl(String authAccessUrl) {
			this.authAccessUrl = authAccessUrl;
		}
	}
}
