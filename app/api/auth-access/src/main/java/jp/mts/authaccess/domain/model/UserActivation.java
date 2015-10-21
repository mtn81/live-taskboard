package jp.mts.authaccess.domain.model;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import jp.mts.base.domain.model.DomainObject;

public class UserActivation extends DomainObject {
	
	private UserActivationId id;
	private UserId userId;
	private Date expireTime;
	
	public UserActivation(
			UserActivationId id, UserId userId, Date expireTime) {
		this.id = id;
		this.userId = userId;
		this.expireTime = expireTime;
	}
	
	public UserActivationId id() {
		return id;
	}
	public UserId userId() {
		return userId;
	}
	public Date expireTime() {
		return expireTime;
	}

	void setUserId(UserId userId) {
		this.userId = userId;
	}

	void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public boolean isExpired() {
		return calendar.systemDate().after(this.expireTime);
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
		UserActivation other = (UserActivation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
