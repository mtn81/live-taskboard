package jp.mts.authaccess.domain.model.social;

public interface SocialAuthDomainService {

	String generateStateToken();
	String authLocation(String stateToken);
	SocialUser loadSocialUser(String authCode);

}
