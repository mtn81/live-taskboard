package jp.mts.authaccess.rest.presentation.model;

import jp.mts.authaccess.application.AuthAppService;

public class AuthRemove {

	//output
	private String authId;
	
	public String getAuthId() {
		return authId;
	}

	public void remove(String authId, AuthAppService authAppService) {
		authAppService.remove(authId);
	}
}
