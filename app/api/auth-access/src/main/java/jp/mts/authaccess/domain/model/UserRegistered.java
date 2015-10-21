package jp.mts.authaccess.domain.model;

import jp.mts.base.domain.model.DomainEvent;

public class UserRegistered extends DomainEvent {
	
	private UserId userId;
	private String email;

	public UserRegistered(UserId userId, String email) {
		this.userId = userId;
		this.email = email;
	}
	
	public UserId getUserId() {
		return userId;
	}
	public String getEmail() {
		return email;
	}
}
