package jp.mts.authaccess.rest;

import javax.servlet.http.HttpServletResponse;

import jp.mts.authaccess.application.AuthAppService;
import jp.mts.authaccess.application.AuthAppService.AuthResult;
import jp.mts.authaccess.application.ErrorType;
import jp.mts.base.application.ApplicationException;
import jp.mts.base.rest.RestResponse;
import jp.mts.base.rest.RestResponse.ApiError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthApi {
	
	@Autowired
	private AuthAppService authService;
	
	@RequestMapping(value="/", method = RequestMethod.POST)
	public RestResponse<AuthView> authenticate(
			@RequestBody AuthenticateRequest request,
			HttpServletResponse response){
		
		try{
			AuthResult authResult = authService.authenticate(request.id, request.password);
			return RestResponse.of(new AuthView(authResult.auth, authResult.user));
		}catch(ApplicationException e){
			if(e.hasErrorOf(ErrorType.AUTH_FAILED)){
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return RestResponse.of(new ApiError(ErrorType.AUTH_FAILED));
			}
			throw e;
		}
	}
}
