package jp.mts.base.config.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class ClientContextAspect {

	@Around("jp.mts.base.config.aspect.AppArchitecture.restApi()")
	public Object checkAuthenticated(ProceedingJoinPoint pjp) throws Throwable {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		String clientId = requestAttributes.getRequest().getHeader("X-ClientId");

		ClientContext.start(clientId);

		return pjp.proceed();
	}
}
