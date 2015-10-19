package jp.mts.authaccess.domain.model;

import java.util.Date;

public class UserActivationPromiseBuilder {
	private UserActivationPromise target;
	
	public UserActivationPromiseBuilder(UserActivationPromise target) {
		this.target = target;
	}
	
	public UserActivationPromiseBuilder setExpireTime(Date expireTime) {
		target.setExpireTime(expireTime);
		return this;
	}

	public UserActivationPromise get() {
		return target;
	}
}
