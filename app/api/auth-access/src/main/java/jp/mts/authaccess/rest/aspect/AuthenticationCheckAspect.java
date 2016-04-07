package jp.mts.authaccess.rest.aspect;

import static jp.mts.authaccess.application.ErrorType.AUTH_NOT_FOUND;
import jp.mts.authaccess.application.AuthAppService;
import jp.mts.authaccess.domain.model.Auth;
import jp.mts.base.application.ApplicationException;
import jp.mts.base.util.AspectUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
@Order(1)
public class AuthenticationCheckAspect {

	@Autowired
	private AuthAppService authAppService;
		

	@Around("jp.mts.base.aspect.AppArchitecture.restApi()")
	public Object checkAuthenticated(ProceedingJoinPoint pjp) throws Throwable {
		String userId = AspectUtils.paramValueOf(pjp, Authenticated.class);
		if (userId == null) {
			return pjp.proceed();
		}

		ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		String authId = requestAttributes.getRequest().getHeader("X-AuthAccess-AuthId");
		
		Auth auth = authAppService.load(authId);
		if(!auth.userId().idValue().equals(userId)){
			throw new ApplicationException(AUTH_NOT_FOUND);
		}

		return pjp.proceed();
	}
}
