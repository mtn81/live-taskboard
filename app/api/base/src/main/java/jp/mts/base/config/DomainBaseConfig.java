package jp.mts.base.config;

import javax.annotation.PostConstruct;

import jp.mts.base.domain.model.DomainCalendar;
import jp.mts.base.domain.model.DomainEventPublisher;
import jp.mts.base.domain.model.DomainObject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBaseConfig {

	@Bean
	public DomainEventPublisher domainEventPublisher(){
		return new DomainEventPublisher();
	}
	@Bean
	public DomainCalendar domainCalendar(){
		return new DomainCalendar();
	}

	@PostConstruct
	public void initialize() {
		DomainObject.setDomainEventPublisher(domainEventPublisher());
		DomainObject.setDomainCalendar(domainCalendar());
	}
}
