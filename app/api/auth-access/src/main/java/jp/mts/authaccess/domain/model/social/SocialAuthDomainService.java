package jp.mts.authaccess.domain.model.social;

import jp.mts.authaccess.domain.model.UserType;

public interface SocialAuthDomainService {

	SocialAuthProvider providerOf(UserType userType);
	String generateStateToken();

	public interface SocialAuthProvider {
		SocialAuthProcess startAuthProcess(SocialAuthProcessId id, String acceptClientUrl, String rejectClientUrl);
		SocialUser loadSocialUser(String... authKeys);
		UserType targetUserType();
	}
}
