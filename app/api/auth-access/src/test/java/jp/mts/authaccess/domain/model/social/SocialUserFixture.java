package jp.mts.authaccess.domain.model.social;

import jp.mts.authaccess.domain.model.UserType;

public class SocialUserFixture {

	private SocialUser target;

	public SocialUserFixture() {
		this(new SocialUserId(UserType.GOOGLE, "u01"), "social@test.jp", "taro");
	}
	public SocialUserFixture(SocialUserId socialUserId, String email, String name) {
		this.target = new SocialUser(socialUserId, email, name);
	}
	
	public SocialUser get() {
		return target;
	}

	public SocialUserFixture setCustomName(String name) {
		target.setCustomName(name);
		return this;
	}

	public SocialUserFixture setCustomEmail(String email) {
		target.setCustomEmail(email);
		return this;
	}

	public SocialUserFixture setOriginalName(String originalName) {
		target.setOriginalName(originalName);
		return this;
	}

	public SocialUserFixture setOriginalEmail(String originalEmail) {
		target.setOriginalEmail(originalEmail);
		return this;
	}

	public SocialUserFixture setUseEmailNotification(boolean useEmailNotification) {
		target.setUseEmailNotification(useEmailNotification);
		return this;
	}

}
