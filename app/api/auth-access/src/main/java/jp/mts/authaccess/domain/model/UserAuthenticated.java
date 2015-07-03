package jp.mts.authaccess.domain.model;

public class UserAuthenticated extends DomainEvent {
	
	private AuthId authId;
	private UserId userId;

	public UserAuthenticated(AuthId authId, UserId userId) {
		this.authId = authId;
		this.userId = userId;
	}
	
	public AuthId getAuthId() {
		return authId;
	}
	public UserId getUserId() {
		return userId;
	}
}
