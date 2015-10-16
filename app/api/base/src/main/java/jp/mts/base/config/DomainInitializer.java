package jp.mts.base.config;

import javax.annotation.PostConstruct;

import jp.mts.base.domain.model.DomainCalendar;
import jp.mts.base.domain.model.DomainEventPublisher;
import jp.mts.base.domain.model.DomainObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainInitializer {
	
	@Autowired
	private DomainEventPublisher domainEventPublisher;
	@Autowired
	private DomainCalendar domainCalendar;
	
	@PostConstruct
	public void initialize() {
		DomainObject.setDomainEventPublisher(domainEventPublisher);
		DomainObject.setDomainCalendar(domainCalendar);
	}
	
}
