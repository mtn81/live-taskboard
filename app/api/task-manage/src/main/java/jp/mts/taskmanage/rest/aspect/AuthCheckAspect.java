package jp.mts.taskmanage.rest.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class AuthCheckAspect {

	@Autowired
	private HttpServletRequest request;

	@Around("jp.mts.base.config.aspect.AppArchitecture.restApi()")
	public Object checkAuthenticated(ProceedingJoinPoint pjp) throws Throwable {
		//ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
//		String authId = requestAttributes.getRequest().getHeader("X-AuthAccess-AuthId");
		String authId = request.getHeader("X-AuthAccess-AuthId");
		System.out.println(authId);
		return pjp.proceed();
	}
}
