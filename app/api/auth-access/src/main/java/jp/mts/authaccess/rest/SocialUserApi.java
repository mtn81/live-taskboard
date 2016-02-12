package jp.mts.authaccess.rest;

import jp.mts.authaccess.rest.presentation.model.SocialUserLoad;
import jp.mts.base.rest.RestResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SocialUserApi {

	@RequestMapping(
		value="/social_users/{userId}", 
		method=RequestMethod.GET)
	public RestResponse<SocialUserLoad> loadSocialUser(
			@PathVariable String userId) {
		SocialUserLoad socialUserLoad = new SocialUserLoad();
		socialUserLoad.load(userId);
		return RestResponse.of(socialUserLoad);
	}


}
