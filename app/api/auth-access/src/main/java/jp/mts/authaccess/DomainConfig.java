package jp.mts.authaccess;

import jp.mts.authaccess.domain.model.AuthRepository;
import jp.mts.authaccess.domain.model.proper.PasswordEncriptionService;
import jp.mts.authaccess.domain.model.proper.ProperAuthenticateService;
import jp.mts.authaccess.domain.model.proper.ProperUserRepository;
import jp.mts.authaccess.domain.model.social.SocialAuthDomainService;
import jp.mts.authaccess.infrastructure.service.OpenIdConnectSocialAuthDomainService;
import jp.mts.authaccess.infrastructure.service.Pbkdf2UserPasswrodEncriptionService;
import jp.mts.base.domain.model.DomainEventPublisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
		return new OpenIdConnectSocialAuthDomainService();
	}
	
}
