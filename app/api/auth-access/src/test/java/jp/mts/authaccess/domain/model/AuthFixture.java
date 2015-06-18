package jp.mts.authaccess.domain.model;

public class AuthFixture {
	
	private Auth auth;
	
	public AuthFixture(){
		auth = new Auth(new AuthId("a01"), new UserId("u01"));
	}
	
	public AuthFixture authId(AuthId authId) {
		auth.setId(authId);
		return this;
	}
	
	public Auth get(){
		return auth; 
	}
}
