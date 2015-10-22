package jp.mts.authaccess.domain.model;

import jp.mts.base.domain.model.DomainEvent;

public class UserRegistered extends DomainEvent {
	
	private UserId userId;
	private UserActivationId userActivationId;
	private String email;

	public UserRegistered(
			UserId userId, 
			UserActivationId userActivationId,
			String email) {
		this.userId = userId;
		this.userActivationId = userActivationId;
		this.email = email;
	}
	
	public UserId getUserId() {
		return userId;
	}
	public UserActivationId getActivationId() {
		return userActivationId;
	}
	public String getEmail() {
		return email;
	}
}
