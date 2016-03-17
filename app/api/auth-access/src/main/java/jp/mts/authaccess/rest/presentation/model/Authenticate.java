package jp.mts.authaccess.rest.presentation.model;

import jp.mts.authaccess.application.AuthAppService;
import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.proper.ProperUser;

public class Authenticate {
	
	//input
	private String id;
	private String password;

	public void setId(String id) {
		this.id = id;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	//output
	private Auth auth;
	private ProperUser user;
	
	public String getAuthId(){
		return auth.id().value();
	}
	public String getUserId(){
		return auth.userId().idValue();
	}
	public String getUserName(){
		return user.name();
	}
	public String getUserType() {
		return auth.userId().userType().name();
	}
	
	//process
	public void authenticate(AuthAppService authAppService) {
		authAppService.authenticate(id, password, (auth, user) -> {
			this.auth = auth;
			this.user = user;
		});
	}
}
