package jp.mts.widgetstore.rest.aspect;

import static jp.mts.widgetstore.application.ErrorType.NOT_AUTHORIZED;

import javax.servlet.http.HttpServletResponse;

import jp.mts.base.rest.RestResponse;
import jp.mts.base.rest.RestResponse.ApiError;
import jp.mts.base.util.AspectUtils;
import jp.mts.widgetstore.application.AuthorizationAppService;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
@Order(1)
public class AuthenticationCheckAspect {

	private AuthorizationAppService authorizationAppService;
	
	@Around("jp.mts.base.config.aspect.AppArchitecture.restApi()")
	public Object checkAuthenticated(ProceedingJoinPoint pjp) throws Throwable {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		String authId = requestAttributes.getRequest().getHeader("X-AuthAccess-AuthId");
		String groupId = AspectUtils.paramValueOf(pjp, Authorized.class);

		if(authorizationAppService.isAccesible(authId, groupId)) {
			return pjp.proceed();
		} else {
			requestAttributes.getResponse().setStatus(HttpServletResponse.SC_FORBIDDEN);
			return RestResponse.of(new ApiError(NOT_AUTHORIZED));
		}
	}
}
