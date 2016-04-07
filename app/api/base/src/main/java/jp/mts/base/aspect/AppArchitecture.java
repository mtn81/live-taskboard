package jp.mts.base.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AppArchitecture {
	
	@Pointcut("execution(* jp.mts..rest..*Api.*(..))")
	public void restApi() {}

	@Pointcut("execution(* jp.mts..application..*AppService.*(..))")
	public void appService() {}

	@Pointcut("execution(* jp.mts..domain.model..*Repository.*(..))")
	public void repository() {}

	@Pointcut("execution(* jp.mts..application.query..*Query.*(..))")
	public void query() {}
	
	@Pointcut("execution(* jp.mts.libs.event.eventstore.EventStore.*(..)) || "
			+ "execution(* jp.mts.libs.event.EventProcessRecord.*(..)) || "
			+ "execution(* jp.mts.libs.event.mq.MqEventProcessTracker.*(..))")
	public void event() {}
	
}
