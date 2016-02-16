package jp.mts.authaccess.rest.presentation.model;

import jp.mts.authaccess.application.SocialUserAppService;
import jp.mts.authaccess.domain.model.social.SocialUser;

public class SocialUserLoad {
	
	//required services
	private static SocialUserAppService socialUserAppService;

	public static void setSocialAppService(SocialUserAppService socialUserAppService) {
		SocialUserLoad.socialUserAppService = socialUserAppService;
	}

	//output
	private SocialUser user;

	public String getName() {
		return user.name();
	}

	public String getEmail() {
		return user.email();
	}

	public String getOriginalName() {
		return user.originalName();
	}

	public String getOriginalEmail() {
		return user.originalEmail();
	}

	public boolean useEmailNotification() {
		return user.useEmailNotification();
	}

	//process
	public void load(String userId) {
		user = socialUserAppService.loadUser(userId);
	}


}
