package jp.mts.authaccess.domain.model;

import jp.mts.base.domain.model.DomainEvent;

public class UserRegistered extends DomainEvent {
	
	private UserId userId;
	private UserActivationId activationId;
	private String name;
	private String email;

	public UserRegistered(
			UserId userId, 
			UserActivationId activationId,
			String name,
			String email) {
		this.userId = userId;
		this.activationId = activationId;
		this.name = name;
		this.email = email;
	}
	
	public UserId getUserId() {
		return userId;
	}
	public UserActivationId getActivationId() {
		return activationId;
	}
	public String getEmail() {
		return email;
	}
	public String getName() {
		return name;
	}
}
