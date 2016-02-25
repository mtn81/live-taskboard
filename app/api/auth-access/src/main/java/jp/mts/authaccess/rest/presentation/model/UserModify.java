package jp.mts.authaccess.rest.presentation.model;

import jp.mts.authaccess.application.UserAppService;
import jp.mts.authaccess.domain.model.proper.ProperUser;

public class UserModify {
	
	//required service
	private static UserAppService userAppService;
	
	public static void setUserAppService(UserAppService userAppService) {
		UserModify.userAppService = userAppService;
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
	private ProperUser user;
	
	public String getUserId() {
		return user.id().idValue();
	}

	public void modify(String userId) {
		user = userAppService.changeUserAttributes(userId, userName, email, notifyEmail);
	}
	
}
