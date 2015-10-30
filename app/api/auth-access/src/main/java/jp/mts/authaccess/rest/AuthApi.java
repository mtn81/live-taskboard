package jp.mts.authaccess.rest;

import javax.servlet.http.HttpServletResponse;

import jp.mts.authaccess.application.AuthAppService;
import jp.mts.authaccess.application.ErrorType;
import jp.mts.authaccess.rest.presentation.model.AuthLoad;
import jp.mts.authaccess.rest.presentation.model.Authenticate;
import jp.mts.base.application.ApplicationException;
import jp.mts.base.rest.RestResponse;
import jp.mts.base.rest.RestResponse.ApiError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthApi {
	
	@Autowired
	private AuthAppService authService;
	

	@RequestMapping(value="/{authId}", method = RequestMethod.GET)
	public RestResponse<AuthLoad> loadAuth(
			@PathVariable String authId) {
		
		try {
			AuthLoad authLoad = new AuthLoad();
			authLoad.loadAuth(authId, authService);
			return RestResponse.of(authLoad);
		} catch (ApplicationException e) {
			if (e.hasErrorOf(ErrorType.AUTH_NOT_FOUND)) {
				RestResponse.of(new ApiError(ErrorType.AUTH_NOT_FOUND), HttpServletResponse.SC_NOT_FOUND);
			}
			throw e;
		}
	}
	
	@RequestMapping(value="/", method = RequestMethod.POST)
	public RestResponse<Authenticate> authenticate(
			@RequestBody Authenticate authenticate){
		
		try{
			authenticate.authenticate(authService);
			return RestResponse.of(authenticate);
		}catch(ApplicationException e){
			if(e.hasErrorOf(ErrorType.AUTH_FAILED)){
				return RestResponse.of(new ApiError(ErrorType.AUTH_FAILED), HttpServletResponse.SC_BAD_REQUEST);
			}
			throw e;
		}
	}
}
