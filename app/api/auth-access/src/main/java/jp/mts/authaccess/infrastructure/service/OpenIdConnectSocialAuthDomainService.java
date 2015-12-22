package jp.mts.authaccess.infrastructure.service;

import java.math.BigInteger;
import java.security.SecureRandom;

import jp.mts.authaccess.application.ErrorType;
import jp.mts.authaccess.domain.model.UserType;
import jp.mts.authaccess.domain.model.social.SocialAuthDomainService;
import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.authaccess.domain.model.social.SocialUserId;
import jp.mts.base.application.ApplicationException;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
public class OpenIdConnectSocialAuthDomainService implements SocialAuthDomainService {

	private static final String CLIENT_ID = "992563238212-ff6k6vor5i3bkq6d8p8k0j44m0tmufhd.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "LxzUP-U-aTalQYfk5_ttEBWB";
	
	@Override
	public String generateStateToken() {
		return new BigInteger(130, new SecureRandom()).toString(32);
	}

	@Override
	public String authLocation(String stateToken) {
		return fetchEndpointUrl("authorization_endpoint")
				+ "?client_id=" + CLIENT_ID
				+ "&response_type=code"
				+ "&scope=openid%20email%20profile"
				+ "&redirect_uri=http://localhost:9000/api/auth-access/social_auth?accept"
				+ "&state=" + stateToken;
	}

	@Override
	public SocialUser loadSocialUser(String authCode) {
		
		try {
			HttpResponse<JsonNode> idResponse = Unirest
				.post(fetchEndpointUrl("token_endpoint"))
				.field("code", authCode)
				.field("client_id", CLIENT_ID)
				.field("client_secret", CLIENT_SECRET)
				.field("redirect_uri", "http://localhost:9000/api/auth-access/social_auth?accept")
				.field("grant_type", "authorization_code")
				.asJson();
			
			if (idResponse.getStatus() >= 400) {
				throw new ApplicationException(ErrorType.SOCIAL_AUTH_FAILED);
			}
			
			JSONObject idBody = new JSONObject(
					new String(Base64.decodeBase64(idResponse.getBody().getObject().getString("id_token").split("\\.")[1])));
			
			return new SocialUser(
					new SocialUserId(UserType.GOOGLE, (String)idBody.get("sub")), 
					(String)idBody.get("email"), 
					(String)idBody.get("name"));

		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private String fetchEndpointUrl(String key) {
		try {
			HttpResponse<JsonNode> response = Unirest.get("https://accounts.google.com/.well-known/openid-configuration").asJson();
			if (response.getStatus() >= 400) {
				throw new IllegalStateException("google discovery document unavailable.");
			}
			return response.getBody().getObject().getString(key);
		} catch (UnirestException e) {
			throw new RuntimeException(e);
		}
	}

}
