package jp.mts.base.config.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AppArchitecture {
	
	@Pointcut("execution(* jp.mts..application..*AppService.*(..))")
	public void appService() {}

	@Pointcut("execution(* jp.mts..domain.model..*Repository.*(..))")
	public void repository() {}
	
	@Pointcut("execution(* jp.mts.libs.event.eventstore.EventStore.*(..))")
	public void eventStore() {}
	
}
