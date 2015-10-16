package jp.mts.authaccess.domain.model;

import java.util.Date;

public class UserActivation {
	
	private UserActivationId id;
	private UserId userId;
	private Date expireTime;
	
	public UserActivation(UserActivationId id, UserId userId, Date expireTime) {
		this.id = id;
		this.userId = userId;
		this.expireTime = expireTime;
	}
	
	public UserActivationId userActivationId() {
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

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
}
