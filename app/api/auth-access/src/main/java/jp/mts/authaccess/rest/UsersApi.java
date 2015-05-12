package jp.mts.authaccess.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UsersApi {
	
	@RequestMapping(value="/", method=POST)
	public RestResponse create(
			@RequestBody UserRegisterRequest request){
		return new RestResponse();
	}
}
