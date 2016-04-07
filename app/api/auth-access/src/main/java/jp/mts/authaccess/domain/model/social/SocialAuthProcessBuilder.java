package jp.mts.authaccess.domain.model.social;

public class SocialAuthProcessBuilder {

	private SocialAuthProcess target;

	public SocialAuthProcessBuilder(SocialAuthProcess socialAuthProcess) {
		this.target = socialAuthProcess;
	}
	
	public SocialAuthProcessBuilder setFirstUse(boolean firstUse) {
		this.target.setFirstUse(firstUse);;
		return this;
	}
	public SocialAuthProcessBuilder setSocialUserId(SocialUserId socialUserId) {
		this.target.setSocialUserId(socialUserId);
		return this;
	}
	
	public SocialAuthProcess get() {
		return target;
	}
	
}
