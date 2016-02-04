package jp.mts.authaccess.rest.presentation.model;

import jp.mts.authaccess.application.SocialAuthAppService;
import jp.mts.authaccess.domain.model.social.SocialUser;

public class SocialUserLoad {
	//required services
	private static SocialAuthAppService socialAuthAppService;
	
	public static void setSocialAuthAppService(SocialAuthAppService socialAuthAppService) {
		SocialUserLoad.socialAuthAppService = socialAuthAppService;
	}

	//output
	private SocialUser socialUser;

	public String getUserName() {
		return socialUser.name();
	}
	public String getEmail() {
		return socialUser.email();
	}

	public void load(String processId) {
		socialUser = socialAuthAppService.loadSocialUserInProcess(processId);
	}



}
