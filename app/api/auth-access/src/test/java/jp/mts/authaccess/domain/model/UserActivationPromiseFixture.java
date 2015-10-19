package jp.mts.authaccess.domain.model;

import java.util.Date;

public class UserActivationPromiseFixture {

	private UserActivationPromise userActivation;
	
	public UserActivationPromiseFixture() {
		this("activate01");
	}
	public UserActivationPromiseFixture(String activationId) {
		userActivation = new UserActivationPromise(
				new UserActivationId(activationId),
				new UserId("u01"),
				new Date());
	}

	public UserActivationPromiseFixture setUserId(String userId) {
		userActivation.setUserId(new UserId(userId));
		return this;
	}

	public UserActivationPromiseFixture setExpireTime(Date dateTime) {
		userActivation.setExpireTime(dateTime);
		return this;
	}
	
	public UserActivationPromise get() {
		return userActivation;
	}

}
