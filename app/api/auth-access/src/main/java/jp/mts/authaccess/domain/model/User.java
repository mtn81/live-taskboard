package jp.mts.authaccess.domain.model;

import jp.mts.base.domain.model.DomainObject;

import org.apache.commons.lang3.time.DateUtils;


public class User extends DomainObject{
	
	private UserId id;
	private String email;
	private String encryptedPassword;
	private String name;
	private UserStatus status;
	
	public User(UserId id, 
				String email, 
				String encryptedPassword, 
				String name) {
		this.id = id;
		this.email = email;
		this.encryptedPassword = encryptedPassword;
		this.name = name;
		this.status = UserStatus.NEW;
	}
	
	public UserActivationPromise promiseActivate(
			UserActivationId activationId) {

		return new UserActivationPromise(
				activationId, 
				id,
				DateUtils.addHours(calendar.systemDate(), 1));
	}

	public void activate() {
		this.status = UserStatus.ACTIVE;
	}
	
	public UserId id() {
		return this.id;
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
	public UserStatus status() {
		return status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	void setStatus(UserStatus status) {
		this.status = status;
	}
	
	

}
