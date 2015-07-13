package jp.mts.authaccess.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import jp.mts.authaccess.application.UserAppService;
import jp.mts.authaccess.domain.model.User;
import jp.mts.base.rest.RestResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UsersApi {
	
	@Autowired
	private UserAppService userAppService;
	
	@RequestMapping(value="/", method=POST)
	public RestResponse<UserRegisterView> register(
			@RequestBody UserRegisterRequest request){

		User registeredUser = userAppService.register(
				request.userId,
				request.email, 
				request.name, 
				request.password);

		return RestResponse.of(new UserRegisterView(registeredUser));
	}
}
