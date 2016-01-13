package jp.mts.authaccess.infrastructure.service;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javafx.print.PrintColor;
import jp.mts.authaccess.application.ErrorType;
import jp.mts.authaccess.domain.model.UserType;
import jp.mts.authaccess.domain.model.social.SocialAuthDomainService;
import jp.mts.authaccess.domain.model.social.SocialAuthProcess;
import jp.mts.authaccess.domain.model.social.SocialAuthProcessId;
import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.authaccess.domain.model.social.SocialUserId;
import jp.mts.authaccess.infrastructure.service.OAuth1Header.OAuth1SignatureBuilder;
import jp.mts.base.application.ApplicationException;
import jp.mts.base.util.Assertions;
import jp.mts.base.util.MapUtils;

import org.apache.commons.codec.binary.Base64;
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
	
	public HttpSocialAuthDomainService addProvider(SocialAuthProvider provider) {
		providers.put(provider.targetUserType(), provider);
		return this;
	}

	public static class TwitterSocialAuthProvider implements SocialAuthProvider {
		private String appId = "Z3OkrqKvW7OWAKLudkncQpwZe";
		private String appSecret = "4P4WkAiWA1GMWc196YUTTS746BeedCCi3PBTDO8vTHWiUIRvdR";
		private String redirectUri;
		private UserType userType = UserType.TWITTER;
		
		public TwitterSocialAuthProvider(
				String appId, 
				String appSecret,
				String redirectUri) {
			this.appId = appId;
			this.appSecret = appSecret;
			this.redirectUri = redirectUri;
		}

		@Override
		public SocialAuthProcess startAuthProcess(
				SocialAuthProcessId processId,
				String acceptClientUrl,
				String rejectClientUrl) {
			
			return new SocialAuthProcess(
				processId, 
				null,
				authLocation(),
				acceptClientUrl,
				rejectClientUrl,
				userType);
		}
		
		private String authLocation() {
			String httpMethod = "POST";
			String baseUrl = "https://api.twitter.com/oauth/request_token";
			
			OAuth1Header oAuth1Header = new OAuth1Header(appId, null, redirectUri, 
					new OAuth1SignatureBuilder(httpMethod, baseUrl, appSecret));
			
			try {
				HttpResponse<String> response = Unirest.post(baseUrl)
					.header("Authorization", oAuth1Header.buildAuthenticationHeader(null))
					.asString();
				if (response.getStatus() != 200) {
					throw new ApplicationException(ErrorType.SOCIAL_AUTH_FAILED);
				}

				QueryParameters oAuthTokenParams = new QueryParameters(response.getBody());
				if (!Boolean.valueOf(oAuthTokenParams.get("oauth_callback_confirmed"))) {
					throw new ApplicationException(ErrorType.SOCIAL_AUTH_FAILED);
				}
				
				return "https://api.twitter.com/oauth/authenticate?oauth_token=" + oAuthTokenParams.get("oauth_token");
				
			} catch (UnirestException e) {
				throw new ApplicationException(ErrorType.SOCIAL_AUTH_FAILED);
			}
		}

		@Override
		public SocialUser loadSocialUser(String... authKeys) {
			Assertions.assertNonNull(authKeys);
			Assertions.assertTrue(authKeys.length == 2);

			String oAuthVerifier = authKeys[0];
			String oAuthToken = authKeys[1];
			
			try {
				HttpResponse<String> response;
				{
					String httpMethod = "POST";
					String baseUrl = "https://api.twitter.com/oauth/access_token";
					Map<String, String> params = MapUtils.pairs("oauth_verifier", oAuthVerifier);
					
					OAuth1Header oAuth1Header = new OAuth1Header(appId, oAuthToken, redirectUri, 
							new OAuth1SignatureBuilder(httpMethod, baseUrl, appSecret));
					response = Unirest.post(baseUrl)
						.header("Authorization", oAuth1Header.buildAuthenticationHeader(params))
						.header("Content-Type", "application/x-www-form-urlencoded")
						.field("oauth_verifier", oAuthVerifier)
						.asString();
					if (response.getStatus() != 200) {
						throw new ApplicationException(ErrorType.SOCIAL_AUTH_FAILED);
					}
				}
				
				HttpResponse<JsonNode> accountResponse;
				{
					String httpMethod = "GET";
					String baseUrl = "https://api.twitter.com/1.1/account/verify_credentials.json";
				
					QueryParameters oAuthTokenParams = new QueryParameters(response.getBody());
					String newOAuthToken = oAuthTokenParams.get("oauth_token");
					String newOAuthTokenSecret = oAuthTokenParams.get("oauth_token_secret");
				
					OAuth1Header oAuth1Header = new OAuth1Header(appId, newOAuthToken, redirectUri, 
							new OAuth1SignatureBuilder(httpMethod, baseUrl, appSecret, newOAuthTokenSecret));
					accountResponse = Unirest.get(baseUrl)
						.header("Authorization", oAuth1Header.buildAuthenticationHeader(null))
						.asJson();
					if (accountResponse.getStatus() != 200) {
						throw new ApplicationException(ErrorType.SOCIAL_AUTH_FAILED);
					}
				}
				JSONObject account = accountResponse.getBody().getObject();
				return new SocialUser(
						new SocialUserId(UserType.TWITTER, account.getString("id_str")), 
						null, 
						account.getString("name"));
			
			} catch (Exception e) {
				throw new ApplicationException(ErrorType.SOCIAL_AUTH_FAILED, e);
			}
		}

		@Override
		public UserType targetUserType() {
			return userType;
		}
		
	}
	
	private abstract static class OAuth2AuthProvider implements SocialAuthProvider {

		private UserType userType;
		private String appId;
		private String appSecret;
		private String redirectUri;

		public OAuth2AuthProvider(
				UserType userType,
				String appId, 
				String appSecret,
				String redirectUri) {

			if(userType == null) throw new IllegalArgumentException();
			if(appId == null) throw new IllegalArgumentException();
			if(appSecret == null) throw new IllegalArgumentException();
			if(redirectUri == null) throw new IllegalArgumentException();
			
			this.userType = userType;
			this.appId = appId;
			this.appSecret = appSecret;
			this.redirectUri = redirectUri;
		}
		
		
		@Override
		public SocialAuthProcess startAuthProcess(
				SocialAuthProcessId processId,
				String acceptClientUrl,
				String rejectClientUrl) {
			
			String state = generateStateToken();

			return new SocialAuthProcess(
				processId, 
				state,
				authLocation(state),
				acceptClientUrl,
				rejectClientUrl,
				userType);
		}
		
		@Override
		public UserType targetUserType() {
			return userType;
		}
		
		@Override
		public SocialUser loadSocialUser(String... authKeys) {
			Assertions.assertNonNull(authKeys);
			Assertions.assertTrue(authKeys.length == 1);
			return loadSocialUser(authKeys[0]);
		}


		private String generateStateToken() {
			return new BigInteger(130, new SecureRandom()).toString(32);
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
		
		protected abstract String authLocation(String stateToken);
		protected abstract SocialUser loadSocialUser(String authCode);
		
	}

	public static class FacebookSocialAuthProvider extends OAuth2AuthProvider {

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

	public static class YahooSocialAuthProvider extends OAuth2AuthProvider {

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

	public static class GoogleSocialAuthProvider extends OAuth2AuthProvider {

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
}
