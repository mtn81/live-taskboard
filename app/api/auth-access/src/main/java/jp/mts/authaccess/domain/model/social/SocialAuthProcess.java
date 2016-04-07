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
		setStateToken(stateToken);
		setAuthLocation(authLocation);
		setAcceptClientUrl(acceptClientUrl);
		setRejectClientUrl(rejectClientUrl);
		setUserType(userType);
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
		setSocialUserId(socialUserId);
		setFirstUse(firstUse);
	}

	void setStateToken(String stateToken) {
		this.stateToken = stateToken;
	}
	void setAuthLocation(String authLocation) {
		this.authLocation = authLocation;
	}
	void setAcceptClientUrl(String acceptClientUrl) {
		this.acceptClientUrl = acceptClientUrl;
	}
	void setRejectClientUrl(String rejectClientUrl) {
		this.rejectClientUrl = rejectClientUrl;
	}
	void setFirstUse(boolean firstUse) {
		this.firstUse = firstUse;
	}
	void setUserType(UserType userType) {
		this.userType = userType;
	}
	void setSocialUserId(SocialUserId socialUserId) {
		this.socialUserId = socialUserId;
	}


}
