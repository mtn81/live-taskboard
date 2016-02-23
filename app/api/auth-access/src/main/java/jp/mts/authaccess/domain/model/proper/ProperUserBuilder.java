package jp.mts.authaccess.domain.model.proper;

public class ProperUserBuilder {
	
	private ProperUser user;
	
	public ProperUserBuilder(ProperUser user) {
		this.user = user;
	}
	
	public ProperUserBuilder setStatus(ProperUserStatus status) {
		user.setStatus(status);
		return this;
	}
	public ProperUserBuilder setUserActivation(ProperUserActivation activation) {
		user.setUserActivation(activation);
		return this;
	}
	public ProperUserBuilder setUseEmailNotification(boolean useEmailNotification) {
		user.setUseEmailNotification(useEmailNotification);
		return this;
	}
	
	public ProperUser get() {
		return user;
	}
}
