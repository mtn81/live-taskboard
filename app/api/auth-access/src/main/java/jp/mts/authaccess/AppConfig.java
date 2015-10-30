package jp.mts.authaccess;

import jp.mts.authaccess.domain.model.AuthRepository;
import jp.mts.authaccess.domain.model.AuthenticateService;
import jp.mts.authaccess.domain.model.PasswordEncriptionService;
import jp.mts.authaccess.domain.model.UserRepository;
import jp.mts.authaccess.infrastructure.service.Pbkdf2UserPasswrodEncriptionService;
import jp.mts.base.domain.model.DomainEventPublisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	
	@Autowired 
	private UserRepository userRepository;
	@Autowired 
	private AuthRepository authRepository;
	@Autowired
	private DomainEventPublisher domainEventPublisher;
	@Autowired
	private PasswordEncriptionService passwordEncriptionService;
	
	@Bean
	public AuthenticateService authenticateService(){
		return new AuthenticateService(
				userRepository,
				authRepository,
				passwordEncriptionService,
				domainEventPublisher);
	}
	
	
}
