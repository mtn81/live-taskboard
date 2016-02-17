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
	private String customEmail;
	private String customName;
	private boolean useEmailNotification;

	public SocialUser(
			SocialUserId socialUserId, 
			String email, 
			String name) {

		super(socialUserId);
		this.originalEmail = email;
		this.originalName = name;
	}

	@Override
	public UserId userId() {
		return id();
	}
	@Override
	public String email() {
		return customEmail == null ? originalEmail : customEmail;
	}
	@Override
	public String name() {
		return customName == null ? originalName : customName;
	}
	
	public String customName() {
		return customName;
	}
	public String customEmail() {
		return customEmail;
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

	void setCustomEmail(String email) {
		this.customEmail = email;
	}
	void setCustomName(String name) {
		this.customName = name;
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
