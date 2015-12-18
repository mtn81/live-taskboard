package jp.mts.authaccess.rest.presentation.model;

import jp.mts.authaccess.application.AuthAppService;
import jp.mts.authaccess.domain.model.Auth;

public class AuthLoad {

	//output
	private Auth auth;
	
	public String getAuthId() {
		return auth.id().value();
	}
	public String getUserId() {
		return auth.userId().idValue();
	}
	
	//proccess
	public void loadAuth(String authId, AuthAppService authAppService) {
		this.auth = authAppService.load(authId);
	}
	
}
