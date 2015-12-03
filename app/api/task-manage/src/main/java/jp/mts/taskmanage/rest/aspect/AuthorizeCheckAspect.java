package jp.mts.taskmanage.rest.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import jp.mts.base.application.ApplicationException;
import jp.mts.taskmanage.application.ErrorType;
import jp.mts.taskmanage.rest.authorize.GroupAdmin;
import jp.mts.taskmanage.rest.authorize.GroupBelong;
import jp.mts.taskmanage.rest.authorize.Me;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(2)
public class AuthorizeCheckAspect {

	
	@Around("jp.mts.base.config.aspect.AppArchitecture.restApi()")
	public Object checkAuthenticated(ProceedingJoinPoint pjp) throws Throwable {
		
		String memberId = paramValueOf(pjp, Me.class);
		if(memberId != null && !MemberContext.hasMemberId(memberId)){
			throw new ApplicationException(ErrorType.NOT_AUTHORIZED);
		}

		String adminGroupId = paramValueOf(pjp, GroupAdmin.class);
		if(adminGroupId != null && !MemberContext.isGroupAdmin(adminGroupId)){
			throw new ApplicationException(ErrorType.NOT_AUTHORIZED);
		}

		String belongGroupId = paramValueOf(pjp, GroupBelong.class);
		if(belongGroupId != null && !MemberContext.belongs(belongGroupId)){
			throw new ApplicationException(ErrorType.NOT_AUTHORIZED);
		}
		
		return pjp.proceed();
	}

	private String paramValueOf(
			ProceedingJoinPoint pjp, 
			Class<? extends Annotation> annotationType) {
		
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		Annotation[][] paramsAnnotations = method.getParameterAnnotations();

		int paramIndex = -1;
		for(int i=0; i < paramsAnnotations.length; i++) {
			for(Annotation anno : paramsAnnotations[i]) {
				if (anno.annotationType().equals(annotationType)) {
					paramIndex = i;
				}
			};
		};
		return paramIndex >= 0 ? (String)pjp.getArgs()[paramIndex] : null;
	}
	
	
}
