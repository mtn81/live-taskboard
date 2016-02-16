package jp.mts.authaccess.domain.model.social;

public class SocialUserBuilder {

	private SocialUser socialUser;

	public SocialUserBuilder(SocialUser socialUser) {
		this.socialUser = socialUser;
	}
	
	public SocialUserBuilder setName(String name) {
		socialUser.setName(name);
		return this;
	}
	public SocialUserBuilder setEmail(String email) {
		socialUser.setEmail(email);
		return this;
	}
	public SocialUserBuilder setUseEmailNotification(boolean useEmailNotification) {
		socialUser.setUseEmailNotification(useEmailNotification);
		return this;
	}
	
	public SocialUser get() {
		return socialUser;
	}
	
}
