package jp.mts.base.config.aspect;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventPublisher;
import jp.mts.libs.event.eventstore.EventStore;
import jp.mts.libs.event.eventstore.StoredEventSerializer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class EventStoreAspect {
	
	@Autowired
	private DomainEventPublisher domainEventPublisher;
	@Autowired
	private EventStore eventStore;
	@Autowired
	private StoredEventSerializer storedEventSerializer;
	
	@Around("jp.mts.base.config.aspect.AppArchitecture.appService()")
	public Object subscribeEvent(ProceedingJoinPoint pjp) throws Throwable {
		domainEventPublisher.initialize();

		domainEventPublisher.register(DomainEvent.class, e -> {
			eventStore.add(storedEventSerializer.serialize(e));
		});

		Object result = pjp.proceed();
		
		domainEventPublisher.fire();
		
		return result;
	}

}
