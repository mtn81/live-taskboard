package jp.mts.authaccess.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import jp.mts.authaccess.application.UserAppService;
import jp.mts.authaccess.rest.presentation.model.UserActivation;
import jp.mts.base.rest.RestResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ActivationApi {
	
	@Autowired
	private UserAppService userAppService;
	
	@RequestMapping(value="/activate_user", method=POST)
	public RestResponse<UserActivation> activateUser(
			@RequestBody @Valid UserActivation userActivation) {
		
		userActivation.activate(userAppService);
		
		return RestResponse.of(userActivation);
	}
}
