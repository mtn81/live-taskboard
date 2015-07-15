package jp.mts.base.config;

import javax.annotation.PostConstruct;

import jp.mts.base.domain.model.DomainEventPublisher;
import jp.mts.base.domain.model.DomainObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainInitializer {
	
	@Autowired
	private DomainEventPublisher domainEventPublisher;
	
	@PostConstruct
	public void initialize() {
		DomainObject.setDomainEventPublisher(domainEventPublisher);
	}
	
}
