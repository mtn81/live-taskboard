package jp.mts.authaccess.rest;

import javax.annotation.PostConstruct;

import jp.mts.authaccess.application.SocialUserAppService;
import jp.mts.authaccess.rest.aspect.Authenticated;
import jp.mts.authaccess.rest.presentation.model.SocialUserLoad;
import jp.mts.authaccess.rest.presentation.model.SocialUserSave;
import jp.mts.base.rest.RestResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SocialUserApi {
	
	@Autowired
	private SocialUserAppService socialUserAppService;

	@PostConstruct
	public void initialize() {
		SocialUserLoad.setSocialAppService(socialUserAppService);
		SocialUserSave.setSocialUserAppService(socialUserAppService);
	}

	@RequestMapping(
		value="/social_users/{userId}", 
		method=RequestMethod.GET)
	public RestResponse<SocialUserLoad> loadSocialUser(
			@Authenticated @PathVariable String userId) {
		SocialUserLoad socialUserLoad = new SocialUserLoad();
		socialUserLoad.load(userId);
		return RestResponse.of(socialUserLoad);
	}

	@RequestMapping(
		value="/social_users/{userId}",
		method=RequestMethod.PUT)
	public RestResponse<SocialUserSave> save(
			@Authenticated @PathVariable String userId,
			@RequestBody SocialUserSave socialUserSave) {

		socialUserSave.save(userId);
		return RestResponse.of(socialUserSave);
	}

}
