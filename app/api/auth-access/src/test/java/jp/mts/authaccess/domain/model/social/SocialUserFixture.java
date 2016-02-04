package jp.mts.authaccess.domain.model.social;

import jp.mts.authaccess.domain.model.UserType;

public class SocialUserFixture {

	private SocialUser target;

	public SocialUserFixture() {
		this.target = new SocialUser(new SocialUserId(UserType.GOOGLE, "u01"), "social@test.jp", "taro");
	}
	
	public SocialUser get() {
		return target;
	}

	public SocialUserFixture setName(String name) {
		target.setName(name);
		return this;
	}

	public SocialUserFixture setEmail(String email) {
		target.setEmail(email);
		return this;
	}

}
