package jp.mts.authaccess.domain.model.social;

import jp.mts.authaccess.domain.model.UserType;

public interface SocialAuthDomainService {

	SocialAuthProvider providerOf(UserType userType);
	String generateStateToken();

	public interface SocialAuthProvider {
		String authLocation(String stateToken);
		SocialUser loadSocialUser(String authCode);
	}
}
