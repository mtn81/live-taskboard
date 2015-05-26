package jp.mts.authaccess.rest;

import jp.mts.authaccess.domain.model.User;

public class UserRegisterView {
	
	private User user;
	
	public UserRegisterView(User user){
		this.user = user;
	}
	
	public String getId(){
		return user.id().value();
	}
}
