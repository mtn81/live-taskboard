package jp.mts.authaccess.domain.model.social;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;

@DomainEventConfig(eventType="mts:authaccess/SocialUserEntried")
public class SocialUserEntried extends DomainEvent {

	private SocialUserId userId;
	private String name;

	public SocialUserEntried(SocialUserId userId, String name) {
		this.userId = userId;
		this.name = name;
	}

	public SocialUserId userId() {
		return userId;
	}
	public String name() {
		return name;
	}
}
