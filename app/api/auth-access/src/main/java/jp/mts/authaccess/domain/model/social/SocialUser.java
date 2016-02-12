package jp.mts.authaccess.domain.model.social;

import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthId;
import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserAuthenticated;
import jp.mts.authaccess.domain.model.UserEntried;
import jp.mts.authaccess.domain.model.UserId;
import jp.mts.base.domain.model.DomainEntity;

public class SocialUser extends DomainEntity<SocialUserId> implements User {

	private String originalEmail;
	private String originalName;
	private String email;
	private String name;
	private boolean useEmailNotification;

	public SocialUser(
			SocialUserId socialUserId, 
			String email, 
			String name) {

		super(socialUserId);
		this.originalEmail = email;
		this.originalName = name;
		this.email = email;
		this.name = name;
	}

	@Override
	public UserId userId() {
		return id();
	}
	@Override
	public String email() {
		return email;
	}
	@Override
	public String name() {
		return name;
	}
	
	public String originalEmail() {
		return originalEmail;
	}
	public String originalName() {
		return originalName;
	}
	public boolean useEmailNotification() {
		return useEmailNotification;
	}

	public Auth createAuth(AuthId authId) {
		Auth auth = new Auth(authId, id());
		domainEventPublisher.publish(new UserAuthenticated(auth.id(), id()));
		domainEventPublisher.publish(new UserEntried(this));
		return auth;
	}

	void setEmail(String email) {
		this.email = email;
	}
	void setName(String name) {
		this.name = name;
	}
	void setOriginalEmail(String originalEmail) {
		this.originalEmail = originalEmail;
	}
	void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	void setUseEmailNotification(boolean useEmailNotification) {
		this.useEmailNotification = useEmailNotification;
	}

}
