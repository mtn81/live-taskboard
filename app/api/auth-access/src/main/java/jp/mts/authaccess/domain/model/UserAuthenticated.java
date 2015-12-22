package jp.mts.authaccess.domain.model;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;

@DomainEventConfig(eventType="mts:authaccess/UserAuthenticated")
public class UserAuthenticated extends DomainEvent {
	
	private AuthId authId;
	private UserId userId;

	public UserAuthenticated(AuthId authId, UserId userId) {
		this.authId = authId;
		this.userId = userId;
	}
	
	public String getAuthId() {
		return authId.value();
	}
	public String getUserId() {
		return userId.idValue();
	}
}
