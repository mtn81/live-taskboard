package jp.mts.authaccess.rest;

import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.User;

public class AuthView {
	private Auth auth;
	private User user;
	
	public AuthView(Auth auth, User user){
		this.auth = auth;
		this.user = user;
	}
	
	public String getId(){
		return auth.id().value();
	}
	public String getUserId(){
		return auth.userId().value();
	}
	public String getUserName(){
		return user.name();
	}
}
