package jp.mts.authaccess.domain.model.social;

import jp.mts.authaccess.domain.model.UserType;

public interface SocialAuthProvider {
	SocialAuthProcess startAuthProcess(SocialAuthProcessId id, String acceptClientUrl, String rejectClientUrl);
	SocialUser loadSocialUser(String... authKeys);
	UserType targetUserType();
}
