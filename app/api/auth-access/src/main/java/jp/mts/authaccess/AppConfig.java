package jp.mts.authaccess;

import jp.mts.authaccess.domain.model.AuthRepository;
import jp.mts.authaccess.domain.model.AuthenticateService;
import jp.mts.authaccess.domain.model.DomainEventPublisher;
import jp.mts.authaccess.domain.model.PasswordEncriptionService;
import jp.mts.authaccess.domain.model.UserRepository;
import jp.mts.authaccess.infrastructure.service.Pbkdf2UserPasswrodEncriptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	
	@Autowired ApplicationContext applicationContext;
	
	@Bean
	public PasswordEncriptionService passwordEncriptionService(){
		return new Pbkdf2UserPasswrodEncriptionService();
	}
	
	@Bean
	public AuthenticateService authenticateService(){
		return new AuthenticateService(
				applicationContext.getBean(UserRepository.class), 
				applicationContext.getBean(AuthRepository.class), 
				passwordEncriptionService(),
				domainEventPublisher());
	}
	
	@Bean
	public DomainEventPublisher domainEventPublisher(){
		return new DomainEventPublisher();
	}

}
