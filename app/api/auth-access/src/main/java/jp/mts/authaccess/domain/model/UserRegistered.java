package jp.mts.authaccess.domain.model;

import jp.mts.base.domain.model.DomainEvent;

public class UserRegistered extends DomainEvent {
	
	private UserId userId;

	public UserRegistered(UserId userId) {
		this.userId = userId;
	}
	
	public UserId getUserId() {
		return userId;
	}
}
