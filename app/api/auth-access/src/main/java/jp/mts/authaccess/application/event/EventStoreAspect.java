package jp.mts.authaccess.application.event;

import jp.mts.authaccess.domain.model.DomainEvent;
import jp.mts.authaccess.domain.model.DomainEventPublisher;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EventStoreAspect {
	
	@Autowired
	private DomainEventPublisher domainEventPublisher;
	@Autowired
	private EventStore eventStore;
	@Autowired
	private StoredEventSerializer storedEventSerializer;
	
	@Around("execution(* jp.mts.authaccess.application..*AppService.*(..))")
	public Object subscribeEvent(ProceedingJoinPoint pjp) throws Throwable {
		domainEventPublisher.register(DomainEvent.class, e -> {
			eventStore.add(storedEventSerializer.serialize(e));
		});
		return pjp.proceed();
	}

}
