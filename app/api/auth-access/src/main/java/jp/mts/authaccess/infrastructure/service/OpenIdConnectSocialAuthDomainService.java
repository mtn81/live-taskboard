package jp.mts.authaccess.infrastructure.service;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.springframework.stereotype.Service;

import jp.mts.authaccess.domain.model.social.SocialAuthDomainService;
import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.authaccess.domain.model.social.SocialUserId;
import jp.mts.authaccess.domain.model.social.SocialUserType;

@Service
public class OpenIdConnectSocialAuthDomainService implements SocialAuthDomainService {

	@Override
	public String generateStateToken() {
		return new BigInteger(130, new SecureRandom()).toString(32);
	}

	@Override
	public String authLocation(String stateToken) {
		return "https://accounts.google.com/o/oauth2/v2/auth"
				+ "?client_id=992563238212-ff6k6vor5i3bkq6d8p8k0j44m0tmufhd.apps.googleusercontent.com"
				+ "&response_type=code"
				+ "&scope=openid%20email"
				+ "&redirect_uri=http://localhost:9000/api/auth-access/social_auth?accept"
				+ "&state=" + stateToken;
	}

	@Override
	public SocialUser loadSocialUser(String authCode) {
		return new SocialUser(
				new SocialUserId(SocialUserType.GOOGLE, "googole taro"), 
				"social@test.jp", 
				"social taro");
	}

}
