package jp.mts.authaccess.infrastructure.service.socialauth;

import java.util.Map;

import jp.mts.authaccess.application.ErrorType;
import jp.mts.authaccess.domain.model.UserType;
import jp.mts.authaccess.domain.model.social.SocialAuthProcess;
import jp.mts.authaccess.domain.model.social.SocialAuthProcessId;
import jp.mts.authaccess.domain.model.social.SocialAuthProvider;
import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.authaccess.domain.model.social.SocialUserId;
import jp.mts.authaccess.infrastructure.service.socialauth.OAuth1Header.OAuth1SignatureBuilder;
import jp.mts.base.application.ApplicationException;
import jp.mts.base.util.Assertions;
import jp.mts.base.util.MapUtils;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class TwitterSocialAuthProvider implements SocialAuthProvider {
	private String appId;
	private String appSecret;
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