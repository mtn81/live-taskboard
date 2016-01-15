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
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

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
			HttpResponse<String> response = createPostWithOAuth1(
					"https://api.twitter.com/oauth/access_token", 
					MapUtils.pairs("oauth_verifier", oAuthVerifier), 
					oAuthToken,
					null).asString();

			if (response.getStatus() != 200) {
				throw new ApplicationException(ErrorType.SOCIAL_AUTH_FAILED);
			}
			
			
			QueryParameters oAuthTokenParams = new QueryParameters(response.getBody());
			
			HttpResponse<JsonNode> accountResponse = createGetWithOAuth1(
					"https://api.twitter.com/1.1/account/verify_credentials.json", 
					null, 
					oAuthTokenParams.get("oauth_token"), 
					oAuthTokenParams.get("oauth_token_secret")).asJson();

			if (accountResponse.getStatus() != 200) {
				throw new ApplicationException(ErrorType.SOCIAL_AUTH_FAILED);
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
	
	private HttpRequestWithBody createPostWithOAuth1(
			String baseUrl, 
			Map<String, String> params,
			String oAuthToken,
			String oAuthTokenSecret) {
		
		OAuth1Header oAuth1Header = new OAuth1Header(appId, oAuthToken, redirectUri, 
				new OAuth1SignatureBuilder("POST", baseUrl, appSecret, oAuthTokenSecret));
		HttpRequestWithBody request = Unirest.post(baseUrl)
			.header("Authorization", oAuth1Header.buildAuthenticationHeader(params))
			.header("Content-Type", "application/x-www-form-urlencoded");
		
		params.forEach((key, value) -> {
			request.field(key, value);
		});
		
		return request;
	}
	
	private GetRequest createGetWithOAuth1(
			String baseUrl, 
			Map<String, String> params,
			String oAuthToken,
			String oAuthTokenSecret) {
		
		OAuth1Header oAuth1Header = new OAuth1Header(appId, oAuthToken, redirectUri, 
				new OAuth1SignatureBuilder("GET", baseUrl, appSecret, oAuthTokenSecret));

		return Unirest.get(baseUrl)
			.header("Authorization", oAuth1Header.buildAuthenticationHeader(params));
	}
	
}