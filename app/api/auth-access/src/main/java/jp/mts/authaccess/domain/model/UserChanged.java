package jp.mts.authaccess.domain.model;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;

@DomainEventConfig(eventType="mts:authaccess/UserChanged")
public class UserChanged extends DomainEvent {
	
	private User user;
	
	public UserChanged(User user) {
		this.user = user;
	}

	public String getUserId() {
		return user.userId().idValue();
	}
	public String getName() {
		return user.name();
	}
	public String getEmail() {
		return user.email();
	}
	public boolean getNotifyEmail() {
		return user.useEmailNotification();
	}
}
