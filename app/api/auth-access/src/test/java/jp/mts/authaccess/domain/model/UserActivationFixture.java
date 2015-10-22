package jp.mts.authaccess.domain.model;

import java.util.Date;

public class UserActivationFixture {

	private UserActivation userActivation;
	
	public UserActivationFixture() {
		this("activate01");
	}
	public UserActivationFixture(String activationId) {
		userActivation = new UserActivation(
				new UserActivationId(activationId),
				new Date());
	}

	public UserActivationFixture setExpireTime(Date dateTime) {
		userActivation.setExpireTime(dateTime);
		return this;
	}
	
	public UserActivation get() {
		return userActivation;
	}

}
