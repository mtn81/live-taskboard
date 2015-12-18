package jp.mts.authaccess.domain.model.social;

import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthId;
import jp.mts.authaccess.domain.model.UserAuthenticated;
import jp.mts.base.domain.model.DomainEntity;

public class SocialUser extends DomainEntity<SocialUserId>{

	private String email;
	private String name;

	public SocialUser(
			SocialUserId socialUserId, String email, String name) {
		super(socialUserId);
		this.email = email;
		this.name = name;
	}

	public String email() {
		return email;
	}
	public String name() {
		return name;
	}

	public Auth createAuth(AuthId authId) {
		Auth auth = new Auth(authId, id());
		domainEventPublisher.publish(new UserAuthenticated(auth.id(), id()));
		domainEventPublisher.publish(new SocialUserEntried(id(), name()));
		return auth;
	}
}
