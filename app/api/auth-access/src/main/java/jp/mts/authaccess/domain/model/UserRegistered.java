package jp.mts.authaccess.domain.model;

public class UserRegistered extends DomainEvent {
	
	private UserId userId;

	public UserRegistered(UserId userId) {
		this.userId = userId;
	}
	
	public UserId getUserId() {
		return userId;
	}
}
