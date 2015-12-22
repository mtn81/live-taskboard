package jp.mts.authaccess.domain.model;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;

@DomainEventConfig(eventType="mts:authaccess/UserEntried")
public class UserEntried extends DomainEvent {
	
	private User user;
	
	public UserEntried(User user) {
		this.user = user;
	}

	public String getUserId() {
		return user.userId().idValue();
	}
	public String getType() {
		return user.userId().userType().name();
	}
	public String getName() {
		return user.name();
	}
	public String getEmail() {
		return user.email();
	}

}
