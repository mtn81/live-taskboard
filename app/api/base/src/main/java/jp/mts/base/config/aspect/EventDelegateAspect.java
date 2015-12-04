package jp.mts.base.config.aspect;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventPublisher;
import jp.mts.libs.event.EventService;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class EventDelegateAspect {
	
	@Autowired
	private DomainEventPublisher domainEventPublisher;
	@Autowired
	private EventService eventService;
	
	@Around("jp.mts.base.config.aspect.AppArchitecture.appService()")
	public Object subscribeEvent(ProceedingJoinPoint pjp) throws Throwable {
		domainEventPublisher.initialize();
		domainEventPublisher.register(DomainEvent.class, eventService::delegate);

		Object result = pjp.proceed();
		
		domainEventPublisher.fire();
		
		return result;
	}

}
