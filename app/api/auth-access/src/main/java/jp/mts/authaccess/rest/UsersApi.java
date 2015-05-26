package jp.mts.authaccess.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import jp.mts.authaccess.application.UserService;
import jp.mts.authaccess.domain.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UsersApi {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/", method=POST)
	public RestResponse<UserRegisterView> register(
			@RequestBody UserRegisterRequest request){

		User registeredUser = userService.register(
				request.userId,
				request.email, 
				request.name, 
				request.password);

		return new RestResponse<UserRegisterView>(
				new UserRegisterView(registeredUser));
	}
}
