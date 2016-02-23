package jp.mts.authaccess.rest.presentation.model;

import jp.mts.authaccess.application.UserAppService;
import jp.mts.authaccess.domain.model.proper.ProperUser;

public class UserLoad {
	//required services
	private static UserAppService userAppService;
	
	public static void setUserAppService(UserAppService userAppService) {
		UserLoad.userAppService = userAppService;
	}

	//output
	ProperUser user;

	public String getUserId() {
		return user.id().value();
	}

	public String getName() {
		return user.name();
	}
	public String getEmail() {
		return user.email();
	}
	public boolean isNotifyEmail() {
		return user.useEmailNotification();
	}

	//process
	public void load(String userId) {
		user = userAppService.loadUserById(userId);
	}

}
