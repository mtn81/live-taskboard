package jp.mts.authaccess.rest;

import jp.mts.authaccess.application.AuthService;
import jp.mts.authaccess.domain.model.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthApi {
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping(method = RequestMethod.POST)
	public RestResponse<AuthView> authenticate(@RequestBody AuthenticateRequest request){
		Auth auth = authService.authenticate(request.id, request.password);
		return new RestResponse<AuthView>(new AuthView(auth));
	}
}
