package jp.mts.authaccess.domain.model;

import java.util.Date;

public class UserActivationBuilder {
	private UserActivation target;
	
	public UserActivationBuilder(UserActivation target) {
		this.target = target;
	}
	
	public UserActivationBuilder setExpireTime(Date expireTime) {
		target.setExpireTime(expireTime);
		return this;
	}

	public UserActivation get() {
		return target;
	}
}
