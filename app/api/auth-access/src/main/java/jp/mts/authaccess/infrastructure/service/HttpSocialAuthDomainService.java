package jp.mts.authaccess.infrastructure.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import jp.mts.authaccess.application.ErrorType;
import jp.mts.authaccess.domain.model.UserType;
import jp.mts.authaccess.domain.model.social.SocialAuthDomainService;
import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.authaccess.domain.model.social.SocialUserId;
import jp.mts.base.application.ApplicationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
public class HttpSocialAuthDomainService implements SocialAuthDomainService {
	private Map<UserType, SocialAuthProvider> providers = new HashMap<>();

	@Override
	public SocialAuthProvider providerOf(UserType userType) {
		if(!providers.containsKey(userType)) 
			throw new IllegalArgumentException();
		return providers.get(userType);
	}
	
	@Override
	public String generateStateToken() {
		return new BigInteger(130, new SecureRandom()).toString(32);
	}
	
	public void addProvider(UserType userType, SocialAuthProvider provider) {
		providers.put(userType, provider);
	}

	public static class TwitterSocialAuthProvider implements SocialAuthProvider {
		
		private String redirectUri;
		
		public TwitterSocialAuthProvider(String redirectUri) {
			this.redirectUri = redirectUri;
		}

		@Override
		public String authLocation(String stateToken) {
			try {
				HttpResponse<String> response = Unirest.post("https://api.twitter.com/oauth/request_token")
					.header("Authorization", "OAuth "
							+ "oauth_callback=\"" + redirectUri + "\","
							+ "oauth_consumer_key=\"Z3OkrqKvW7OWAKLudkncQpwZe\","
							+ "oauth_nounce=\"" + RandomStringUtils.randomAlphanumeric(32)  + "\","
							+ "oauth_signature=\"yyy\","
							+ "oauth_signature_method=\"HMAC-SHA1\","
							+ "oauth_timestamp=\"" + Instant.now().getEpochSecond() + "\""
							+ "oauth_version=\"1.0\"")
					.asString();
				if (response.getStatus() != 200) {
					throw new ApplicationException(ErrorType.SOCIAL_AUTH_FAILED);
				}
				String body = response.getBody();
				
				System.out.println(body);
				
			} catch (UnirestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SocialUser loadSocialUser(String authCode) {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	}
	
	private abstract static class OAuth2AuthProvider implements SocialAuthProvider {
		
		private String appId;
		private String appSecret;
		private String redirectUri;

		public OAuth2AuthProvider(
				String appId, 
				String appSecret,
				String redirectUri) {

			if(appId == null) throw new IllegalArgumentException();
			if(appSecret == null) throw new IllegalArgumentException();
			if(redirectUri == null) throw new IllegalArgumentException();
			
			this.appId = appId;
			this.appSecret = appSecret;
			this.redirectUri = redirectUri;
		}
		
		protected String makeAuthLocation(
				String endpointUri,
				String stateToken, 
				String scope, 
				String... additionalParams) {
			StringBuilder sb = new StringBuilder(endpointUri)
				.append("?client_id=").append(appId)
				.append("&redirect_uri=").append(redirectUri)
				.append("&state=").append(stateToken)
				.append("&response_type=code")
				.append("&scope=").append(scope);
			if (additionalParams != null) {
				for (String param : additionalParams) {
					sb.append("&").append(param);
				}
			}
			return sb.toString();
		}
		
		protected JSONObject doUnirest(UnirestExecutor executor) {
			try {
				HttpResponse<JsonNode> response = executor.execute();
				if (response.getStatus() >= 400) {
					throw new ApplicationException(ErrorType.SOCIAL_AUTH_FAILED);
				}
				return response.getBody().getObject();
			} catch (Exception e) {
				throw new RuntimeException(e);
				
			}
		}
		
		@FunctionalInterface
		protected interface UnirestExecutor {
			HttpResponse<JsonNode> execute() throws UnirestException;
		}
		
		protected String appId() { return appId; }
		protected String appSecret() { return appSecret; }
		protected String redirectUri() { return redirectUri; }
		
	}

	public static class FacebookSocialAuthProvider extends OAuth2AuthProvider {

		public FacebookSocialAuthProvider(
				String appId, 
				String appSecret,
				String redirectUri) {

			super(appId, appSecret, redirectUri);
		}

		@Override
		public String authLocation(String stateToken) {
			return makeAuthLocation(
					"https://www.facebook.com/dialog/oauth",
					stateToken, 
					"email%20public_profile");
		}

		@Override
		public SocialUser loadSocialUser(String authCode) {
			
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

	public static class YahooSocialAuthProvider extends OAuth2AuthProvider {

		public YahooSocialAuthProvider(
				String appId, 
				String appSecret,
				String redirectUri) {
			super(appId, appSecret, redirectUri);
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
		public SocialUser loadSocialUser(String authCode) {
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

	public static class GoogleSocialAuthProvider extends OAuth2AuthProvider {

		public GoogleSocialAuthProvider(
				String appId, 
				String appSecret,
				String redirectUri) {
			super(appId, appSecret, redirectUri);
		}

		@Override
		public String authLocation(String stateToken) {
			return makeAuthLocation(
					fetchEndpointUrl("authorization_endpoint"), 
					stateToken, 
					"openid%20email%20profile");
		}
	
		@Override
		public SocialUser loadSocialUser(String authCode) {
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
}
