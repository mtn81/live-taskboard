package jp.mts.authaccess;

import jp.mts.authaccess.domain.model.AuthRepository;
import jp.mts.authaccess.domain.model.UserType;
import jp.mts.authaccess.domain.model.proper.PasswordEncriptionService;
import jp.mts.authaccess.domain.model.proper.ProperAuthenticateService;
import jp.mts.authaccess.domain.model.proper.ProperUserRepository;
import jp.mts.authaccess.domain.model.social.SocialAuthDomainService;
import jp.mts.authaccess.infrastructure.service.HttpSocialAuthDomainService;
import jp.mts.authaccess.infrastructure.service.HttpSocialAuthDomainService.FacebookSocialAuthProvider;
import jp.mts.authaccess.infrastructure.service.HttpSocialAuthDomainService.GoogleSocialAuthProvider;
import jp.mts.authaccess.infrastructure.service.HttpSocialAuthDomainService.TwitterSocialAuthProvider;
import jp.mts.authaccess.infrastructure.service.HttpSocialAuthDomainService.YahooSocialAuthProvider;
import jp.mts.base.domain.model.DomainEventPublisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class DomainConfig {
	
	@Autowired 
	private ProperUserRepository userRepository;
	@Autowired 
	private AuthRepository authRepository;
	@Autowired
	private DomainEventPublisher domainEventPublisher;
	@Autowired
	private PasswordEncriptionService passwordEncriptionService;
	@Autowired
	private SocialSettings socialSettings;
	
	@Bean
	public ProperAuthenticateService authenticateService(){
		return new ProperAuthenticateService(
				userRepository,
				authRepository,
				passwordEncriptionService,
				domainEventPublisher);
	}
	
	@Bean
	public SocialAuthDomainService socialAuthDomainService() {
		return new HttpSocialAuthDomainService()
			.addProvider( 
				new GoogleSocialAuthProvider(
						socialSettings.getGoogleAppId(), 
						socialSettings.getGoogleAppSecret(),
						socialSettings.getSocialLoginRedirect()))
			.addProvider( 
				new YahooSocialAuthProvider(
						socialSettings.getYahooAppId(), 
						socialSettings.getYahooAppSecret(),
						socialSettings.getSocialLoginRedirect()))
			.addProvider( 
				new FacebookSocialAuthProvider(
						socialSettings.getFacebookAppId(), 
						socialSettings.getFacebookAppSecret(),
						socialSettings.getSocialLoginRedirect()))
			.addProvider( 
				new TwitterSocialAuthProvider(
						socialSettings.getTwitterAppId(),
						socialSettings.getTwitterAppSecret(),
						socialSettings.getSocialLoginOauth1Redirect()));
	}
	
	
	@Component
	@ConfigurationProperties("social")
	public static class SocialSettings {
		private String googleAppId;
		private String googleAppSecret;
		private String yahooAppId;
		private String yahooAppSecret;
		private String facebookAppId;
		private String facebookAppSecret;
		private String twitterAppId;
		private String twitterAppSecret;
		private String socialLoginRedirect;
		private String socialLoginOauth1Redirect;

		public String getSocialLoginRedirect() {
			return socialLoginRedirect;
		}
		public void setSocialLoginRedirect(String socialLoginRedirect) {
			this.socialLoginRedirect = socialLoginRedirect;
		}
		public String getGoogleAppId() {
			return googleAppId;
		}
		public void setGoogleAppId(String googleAppId) {
			this.googleAppId = googleAppId;
		}
		public String getGoogleAppSecret() {
			return googleAppSecret;
		}
		public void setGoogleAppSecret(String googleAppSecret) {
			this.googleAppSecret = googleAppSecret;
		}
		public String getYahooAppId() {
			return yahooAppId;
		}
		public void setYahooAppId(String yahooAppId) {
			this.yahooAppId = yahooAppId;
		}
		public String getYahooAppSecret() {
			return yahooAppSecret;
		}
		public void setYahooAppSecret(String yahooAppSecret) {
			this.yahooAppSecret = yahooAppSecret;
		}
		public String getFacebookAppId() {
			return facebookAppId;
		}
		public void setFacebookAppId(String facebookAppId) {
			this.facebookAppId = facebookAppId;
		}
		public String getFacebookAppSecret() {
			return facebookAppSecret;
		}
		public void setFacebookAppSecret(String facebookAppSecret) {
			this.facebookAppSecret = facebookAppSecret;
		}
		public String getSocialLoginOauth1Redirect() {
			return socialLoginOauth1Redirect;
		}
		public void setSocialLoginOauth1Redirect(String socialLoginOauth1Redirect) {
			this.socialLoginOauth1Redirect = socialLoginOauth1Redirect;
		}
		public String getTwitterAppId() {
			return twitterAppId;
		}
		public void setTwitterAppId(String twitterAppId) {
			this.twitterAppId = twitterAppId;
		}
		public String getTwitterAppSecret() {
			return twitterAppSecret;
		}
		public void setTwitterAppSecret(String twitterAppSecret) {
			this.twitterAppSecret = twitterAppSecret;
		}
	}
	
}
