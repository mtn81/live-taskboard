package jp.mts.authaccess.domain.model;

public class UserBuilder {
	
	private User user;
	
	public UserBuilder(User user) {
		this.user = user;
	}
	
	public UserBuilder setStatus(UserStatus status) {
		user.setStatus(status);
		return this;
	}
	public UserBuilder setUserActivation(UserActivation activation) {
		user.setUserActivation(activation);
		return this;
	}
	
	public User get() {
		return user;
	}
}
