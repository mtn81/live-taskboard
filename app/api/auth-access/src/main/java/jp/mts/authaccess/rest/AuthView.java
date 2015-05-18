package jp.mts.authaccess.rest;

import jp.mts.authaccess.domain.model.Auth;

public class AuthView {
	private Auth auth;
	
	public AuthView(Auth auth){
		this.auth = auth;
	}
	
	public String getId(){
		return auth.id();
	}
	public String getUserName(){
		return auth.name();
	}
}
