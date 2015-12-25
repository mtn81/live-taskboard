package jp.mts.authaccess.domain.model.social;

import jp.mts.authaccess.domain.model.UserType;
import jp.mts.base.domain.model.DomainEntity;

public class SocialAuthProcess extends DomainEntity<SocialAuthProcessId>{
	
	private String stateToken;
	private String authLocation;
	private String acceptClientUrl;
	private String rejectClientUrl;
	private boolean firstUse;
	private UserType userType;

	private SocialUserId socialUserId;

	public SocialAuthProcess(
			SocialAuthProcessId authProcessId, 
			String stateToken,
			String authLocation,
			String acceptClientUrl,
			String rejectClientUrl,
			UserType userType) {
		super(authProcessId);
		this.stateToken = stateToken;
		this.authLocation = authLocation;
		this.acceptClientUrl = acceptClientUrl;
		this.rejectClientUrl = rejectClientUrl;
		this.userType = userType;
	}

	public String stateToken() {
		return stateToken;
	}

	public String authLocation() {
		return authLocation;
	}
	
	public String acceptClientUrl() {
		return acceptClientUrl;
	}
	public String rejectClientUrl() {
		return rejectClientUrl;
	}
	public boolean firstUse() {
		return firstUse;
	}
	public UserType userType() {
		return userType;
	}
	public SocialUserId socialUserId() {
		return socialUserId;
	}

	public void associateUser(SocialUser socialUser, boolean firstUse) {
		this.socialUserId = socialUser.id();
		this.firstUse = firstUse;
	}


}
