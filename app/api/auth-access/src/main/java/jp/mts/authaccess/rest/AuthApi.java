package jp.mts.authaccess.rest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthApi {
	
	@RequestMapping(method = RequestMethod.POST)
	public RestResponse authenticate(@RequestBody AuthenticateRequest request){
		Auth auth = new Auth();
		auth.id = "hoge";
		auth.userName = "foo";
		return new RestResponse(auth);
	}
}
