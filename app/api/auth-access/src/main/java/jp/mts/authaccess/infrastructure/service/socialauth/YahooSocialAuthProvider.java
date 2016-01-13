package jp.mts.authaccess.infrastructure.service.socialauth;

import jp.mts.authaccess.domain.model.UserType;
import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.authaccess.domain.model.social.SocialUserId;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

public class YahooSocialAuthProvider extends OAuth2AuthProvider {

	public YahooSocialAuthProvider(
			String appId, 
			String appSecret,
			String redirectUri) {
		super(UserType.YAHOO, appId, appSecret, redirectUri);
	}

	@Override
	public String authLocation(String stateToken) {
		return makeAuthLocation(
				"https://auth.login.yahoo.co.jp/yconnect/v1/authorization",
				stateToken, 
				"openid%20email%20profile",
				"bail=1");
	}

	@Override
	protected SocialUser loadSocialUser(String authCode) {
		JSONObject accessToken = doUnirest(() -> {
			return Unirest.post("https://auth.login.yahoo.co.jp/yconnect/v1/token")
				.header("Content-Type", "application/x-www-form-urlencoded")
				.basicAuth(appId(), appSecret())
				.field("code", authCode)
				.field("grant_type", "authorization_code")
				.field("redirect_uri", redirectUri())
				.asJson();
		});
		JSONObject userInfo = doUnirest(() -> {
			return Unirest.get("https://userinfo.yahooapis.jp/yconnect/v1/attribute")
				.header("Authorization", "Bearer " + accessToken.get("access_token"))
				.queryString("schema", "openid")
				.asJson();
		});

		return new SocialUser(
				new SocialUserId(UserType.YAHOO, (String)userInfo.get("user_id")), 
				(String)userInfo.get("email"), 
				(String)userInfo.get("name"));
	}
}