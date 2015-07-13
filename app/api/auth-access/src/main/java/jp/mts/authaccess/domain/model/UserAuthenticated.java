package jp.mts.authaccess.domain.model;

import jp.mts.base.domain.model.DomainEvent;

public class UserAuthenticated extends DomainEvent {
	
	private AuthId authId;
	private UserId userId;

	public UserAuthenticated(AuthId authId, UserId userId) {
		this.authId = authId;
		this.userId = userId;
	}
	
	public AuthId authId() {
		return authId;
	}
	public UserId userId() {
		return userId;
	}
}
