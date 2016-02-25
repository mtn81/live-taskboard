package jp.mts.authaccess.rest.presentation.model;

import jp.mts.authaccess.application.SocialUserAppService;
import jp.mts.authaccess.domain.model.social.SocialUser;

public class SocialUserSave {

	//required service
	private static SocialUserAppService socialUserAppService;

	public static void setSocialUserAppService(
			SocialUserAppService socialUserAppService) {

		SocialUserSave.socialUserAppService = socialUserAppService;
	}
	
	
	//input
	private String userName;
	private String email;
	private boolean notifyEmail;

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setNotifyEmail(boolean notifyEmail) {
		this.notifyEmail = notifyEmail;
	}

	//output
	private SocialUser socialUser;

	public String getUserId() {
		return socialUser.userId().idValue();
	}

	//process
	public void save(String userId) {
		this.socialUser = socialUserAppService.changeUserAttributes(
				userId, userName, email, notifyEmail);
	}
	

}
