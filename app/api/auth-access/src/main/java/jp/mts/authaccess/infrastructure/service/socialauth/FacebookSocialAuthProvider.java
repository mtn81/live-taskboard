package jp.mts.authaccess.infrastructure.service.socialauth;

import jp.mts.authaccess.domain.model.UserType;
import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.authaccess.domain.model.social.SocialUserId;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

public class FacebookSocialAuthProvider extends OAuth2AuthProvider {

	public FacebookSocialAuthProvider(
			String appId, 
			String appSecret,
			String redirectUri) {

		super(UserType.FACEBOOK, appId, appSecret, redirectUri);
	}

	@Override
	public String authLocation(String stateToken) {
		return makeAuthLocation(
				"https://www.facebook.com/dialog/oauth",
				stateToken, 
				"email%20public_profile");
	}

	@Override
	protected SocialUser loadSocialUser(String authCode) {
		
		JSONObject accessToken = doUnirest(() -> {
			return Unirest.get("https://graph.facebook.com/v2.5/oauth/access_token")
					.queryString("client_id", appId())
					.queryString("client_secret", appSecret())
					.queryString("code", authCode)
					.queryString("redirect_uri", redirectUri())
					.asJson();
		});
		JSONObject userInfo = doUnirest(() -> {
			return Unirest.get("https://graph.facebook.com/v2.5/me")
				.queryString("access_token", accessToken.get("access_token"))
				.queryString("fields", "id,name,email")
				.asJson();
		});

		return new SocialUser(
				new SocialUserId(UserType.FACEBOOK, (String)userInfo.get("id")), 
				(String)userInfo.get("email"), 
				(String)userInfo.get("name"));
	}
}