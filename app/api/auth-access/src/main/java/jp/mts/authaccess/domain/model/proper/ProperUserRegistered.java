package jp.mts.authaccess.domain.model.proper;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;

@DomainEventConfig(eventType="mts:authaccess/UserRegistered")
public class ProperUserRegistered extends DomainEvent {
	
	private ProperUserId userId;
	private ProperUserActivationId activationId;
	private String name;
	private String email;

	public ProperUserRegistered(
			ProperUserId userId, 
			ProperUserActivationId activationId,
			String name,
			String email) {
		this.userId = userId;
		this.activationId = activationId;
		this.name = name;
		this.email = email;
	}
	
	public String getUserId() {
		return userId.value();
	}
	public String getActivationId() {
		return activationId.value();
	}
	public String getEmail() {
		return email;
	}
	public String getName() {
		return name;
	}
}
