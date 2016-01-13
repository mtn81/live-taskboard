package jp.mts.authaccess.infrastructure.service.socialauth;

import jp.mts.authaccess.domain.model.UserType;
import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.authaccess.domain.model.social.SocialUserId;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

public class GoogleSocialAuthProvider extends OAuth2AuthProvider {

	public GoogleSocialAuthProvider(
			String appId, 
			String appSecret,
			String redirectUri) {
		super(UserType.GOOGLE, appId, appSecret, redirectUri);
	}

	@Override
	public String authLocation(String stateToken) {
		return makeAuthLocation(
				fetchEndpointUrl("authorization_endpoint"), 
				stateToken, 
				"openid%20email%20profile");
	}

	@Override
	protected SocialUser loadSocialUser(String authCode) {
		JSONObject idToken = doUnirest(() -> {
			return Unirest
				.post(fetchEndpointUrl("token_endpoint"))
				.field("code", authCode)
				.field("client_id", appId())
				.field("client_secret", appSecret())
				.field("redirect_uri", redirectUri())
				.field("grant_type", "authorization_code")
				.asJson();
		});
		
		JSONObject idBody = new JSONObject(
				new String(Base64.decodeBase64(idToken.getString("id_token").split("\\.")[1])));
			
		return new SocialUser(
				new SocialUserId(UserType.GOOGLE, (String)idBody.get("sub")), 
				(String)idBody.get("email"), 
				(String)idBody.get("name"));
	}
	
	private String fetchEndpointUrl(String key) {
		return doUnirest(() -> {
			return Unirest.get("https://accounts.google.com/.well-known/openid-configuration").asJson();
		}).getString(key);
	}
	
}