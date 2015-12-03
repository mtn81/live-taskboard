package jp.mts.authaccess.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import jp.mts.authaccess.application.UserAppService;
import jp.mts.authaccess.rest.presentation.model.UserSave;
import jp.mts.base.rest.RestResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UsersApi {
	
	@Autowired
	private UserAppService userAppService;
	
	@RequestMapping(value="/users", method=POST)
	public RestResponse<UserSave> register(
			@RequestBody @Valid UserSave userSave,
			BindingResult result){
		if(result.hasErrors()){
			return RestResponse.of(result);
		}
		userSave.create(userAppService);
		return RestResponse.of(userSave);
	}
	
	@RequestMapping(value="/users/validate", method=POST)
	public RestResponse<Void> validateForRegister(
			@RequestBody @Valid UserSave userSave,
			BindingResult result){
		
		if(result.hasErrors()){
			return RestResponse.of(result);
		}
		userSave.validateForRegister(userAppService);
		return RestResponse.empty();
	}
}
