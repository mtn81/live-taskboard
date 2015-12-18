package jp.mts.authaccess.domain.model.proper;

import jp.mts.base.domain.model.DomainObject;

import org.apache.commons.lang3.time.DateUtils;


public class ProperUser extends DomainObject{
	
	private ProperUserId id;
	private String email;
	private String encryptedPassword;
	private String name;
	private ProperUserStatus status;
	private ProperUserActivation userActivation;
	
	public ProperUser(ProperUserId id, 
				String email, 
				String encryptedPassword, 
				String name) {
		this.id = id;
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
		return true;
	}
	
	public ProperUserId id() {
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
	public ProperUserStatus status() {
		return status;
	}
	public ProperUserActivation userActivation() {
		return userActivation;
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
		ProperUser other = (ProperUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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
}
