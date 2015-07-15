package jp.mts.base.config;

import jp.mts.base.domain.model.DomainEventPublisher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBaseConfig {

	@Bean
	public DomainEventPublisher domainEventPublisher(){
		return new DomainEventPublisher();
	}
}
