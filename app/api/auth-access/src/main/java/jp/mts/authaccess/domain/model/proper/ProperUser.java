package jp.mts.authaccess.domain.model.proper;

import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserEntried;
import jp.mts.base.domain.model.DomainEntity;

import org.apache.commons.lang3.time.DateUtils;


public class ProperUser extends DomainEntity<ProperUserId> implements User {
	
	private String email;
	private String encryptedPassword;
	private String name;
	private ProperUserStatus status;
	private ProperUserActivation userActivation;
	private boolean useEmailNotification;
	
	public ProperUser(
			ProperUserId id, 
			String email, 
			String encryptedPassword, 
			String name) {

		super(id);
		this.email = email;
		this.encryptedPassword = encryptedPassword;
		this.name = name;
		this.status = ProperUserStatus.NEW;
		this.userActivation = new ProperUserActivation(
				new ProperUserActivationId(), 
				DateUtils.addHours(calendar.systemDate(), 1));
	}
	
	public boolean activate() {
		if(userActivation.isExpired()) return false;
		this.status = ProperUserStatus.ACTIVE;
		
		domainEventPublisher.publish(new UserEntried(this));
		return true;
	}
	
	@Override
	public ProperUserId userId() {
		return id();
	}
	public String email() {
		return email;
	}
	public String name() {
		return name;
	}
	public String encryptedPassword() {
		return encryptedPassword;
	}
	public ProperUserStatus status() {
		return status;
	}
	public ProperUserActivation userActivation() {
		return userActivation;
	}
	@Override
	public boolean useEmailNotification() {
		return useEmailNotification;
	}

	void setStatus(ProperUserStatus status) {
		this.status = status;
	}
	void setUserActivation(ProperUserActivation userActivation) {
		this.userActivation = userActivation;
	}
	void setEmail(String email) {
		this.email = email;
	}
	void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}
	void setName(String name) {
		this.name = name;
	}
	void setUseEmailNotification(boolean useEmailNotification) {
		this.useEmailNotification = useEmailNotification;
	}
	


}
