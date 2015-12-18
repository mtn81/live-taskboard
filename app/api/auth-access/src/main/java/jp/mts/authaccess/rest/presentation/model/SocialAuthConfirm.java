package jp.mts.authaccess.rest.presentation.model;

import jp.mts.authaccess.application.SocialAuthAppService;
import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.social.SocialUser;

public class SocialAuthConfirm {

	//required services
	private static SocialAuthAppService socialAuthAppService;
	
	public static void setSocialAuthAppService(
			SocialAuthAppService socialAuthAppService) {
		SocialAuthConfirm.socialAuthAppService = socialAuthAppService;
	}
	
	//output
	private Auth auth;
	private SocialUser socialUser;
	
	public String getAuthId() {
		return auth.id().value();
	}
	public String getUserId() {
		return socialUser.id().idValue();
	}
	public String getUserName() {
		return socialUser.name();
	}
	
	//process
	public void confirm(String processId) {
		socialAuthAppService.confirmAuth(processId, (auth, socialUser) -> {
			this.auth = auth;
			this.socialUser = socialUser;
		});
	}
}
