package jp.mts.authaccess.domain.model;

import java.util.Date;

public class UserActivationFixture {

	private UserActivation userActivation;
	
	public UserActivationFixture(String activationId) {
		userActivation = new UserActivation(
				new UserActivationId(activationId),
				new UserId("u01"),
				new Date());
	}

	public UserActivationFixture setUserId(String userId) {
		userActivation.setUserId(new UserId(userId));
		return this;
	}

	public UserActivationFixture setExpireTime(Date dateTime) {
		userActivation.setExpireTime(dateTime);
		return this;
	}
	
	public UserActivation get() {
		return userActivation;
	}

}
