package jp.mts.taskmanage.rest.aspect;

import javax.servlet.http.HttpServletResponse;

import jp.mts.base.rest.RestResponse;
import jp.mts.base.rest.RestResponse.ApiError;
import jp.mts.taskmanage.application.MemberAuthAppService;
import jp.mts.taskmanage.application.ErrorType;
import jp.mts.taskmanage.domain.model.MemberId;

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
	private MemberAuthAppService authenticationAppService;
	
	@Around("jp.mts.base.config.aspect.AppArchitecture.restApi()")
	public Object checkAuthenticated(ProceedingJoinPoint pjp) throws Throwable {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		String authId = requestAttributes.getRequest().getHeader("X-AuthAccess-AuthId");

		if (authenticationAppService.validateAuth(authId)) {
			return pjp.proceed();
		} else {
			requestAttributes.getResponse().setStatus(HttpServletResponse.SC_FORBIDDEN);
			return RestResponse.of(new ApiError(ErrorType.NOT_AUTHENTICATED));
		}
	}
}
