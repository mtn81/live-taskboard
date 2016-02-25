package jp.mts.authaccess.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import jp.mts.authaccess.application.UserAppService;
import jp.mts.authaccess.rest.presentation.model.UserLoad;
import jp.mts.authaccess.rest.presentation.model.UserModify;
import jp.mts.authaccess.rest.presentation.model.UserSave;
import jp.mts.base.rest.RestResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UsersApi {
	
	@Autowired
	private UserAppService userAppService;
	
	@PostConstruct
	public void initialize() {
		UserLoad.setUserAppService(userAppService);
		UserSave.setUserAppService(userAppService);
		UserModify.setUserAppService(userAppService);
	}

	@RequestMapping(value="/users/{userId}", method=RequestMethod.GET)
	public RestResponse<UserLoad> loadUser(
			@PathVariable String userId) { //TODO check already logined
		UserLoad userLoad = new UserLoad();
		userLoad.load(userId);
		return RestResponse.of(userLoad);
	}

	@RequestMapping(value="/users/{userId}", method=RequestMethod.PUT)
	public RestResponse<UserModify> modifyUser(
			@PathVariable String userId,
			@RequestBody UserModify userModify) { //TODO check already logined
		userModify.modify(userId);
		return RestResponse.of(userModify);
	}
	
	@RequestMapping(value="/users", method=POST)
	public RestResponse<UserSave> register(
			@RequestBody @Valid UserSave userSave,
			BindingResult result){
		if(result.hasErrors()){
			return RestResponse.of(result);
		}
		userSave.create();
		return RestResponse.of(userSave);
	}
	
	@RequestMapping(value="/users/validate", method=POST)
	public RestResponse<Void> validateForRegister(
			@RequestBody @Valid UserSave userSave,
			BindingResult result){
		
		if(result.hasErrors()){
			return RestResponse.of(result);
		}
		userSave.validateForRegister();
		return RestResponse.empty();
	}

}
