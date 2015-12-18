package jp.mts.authaccess.domain.model.proper;

import java.util.Date;

import jp.mts.authaccess.domain.model.proper.ProperUserActivation;
import jp.mts.authaccess.domain.model.proper.ProperUserActivationId;

public class ProperUserActivationFixture {

	private ProperUserActivation userActivation;
	
	public ProperUserActivationFixture() {
		this("activate01");
	}
	public ProperUserActivationFixture(String activationId) {
		userActivation = new ProperUserActivation(
				new ProperUserActivationId(activationId),
				new Date());
	}

	public ProperUserActivationFixture setExpireTime(Date dateTime) {
		userActivation.setExpireTime(dateTime);
		return this;
	}
	
	public ProperUserActivation get() {
		return userActivation;
	}

}
