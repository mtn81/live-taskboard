package jp.mts.taskmanage.rest.aspect;

import static jp.mts.base.util.AspectUtils.paramValueOf;
import jp.mts.base.application.ApplicationException;
import jp.mts.base.util.AspectUtils;
import jp.mts.taskmanage.application.ErrorType;
import jp.mts.taskmanage.rest.authorize.GroupAdmin;
import jp.mts.taskmanage.rest.authorize.GroupBelong;
import jp.mts.taskmanage.rest.authorize.Me;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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

	
}
