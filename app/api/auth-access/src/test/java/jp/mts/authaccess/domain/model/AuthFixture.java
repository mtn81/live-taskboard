package jp.mts.authaccess.domain.model;

import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthId;
import jp.mts.authaccess.domain.model.proper.ProperUserId;

public class AuthFixture {
	
	private Auth auth;
	
	public AuthFixture(){
		auth = new Auth(new AuthId("a01"), new ProperUserId("u01"));
	}
	
	public AuthFixture authId(AuthId authId) {
		auth.setId(authId);
		return this;
	}
	
	public Auth get(){
		return auth; 
	}
}
