package jp.mts.authaccess.infrastructure.service.socialauth;

import java.math.BigInteger;
import java.security.SecureRandom;

import jp.mts.authaccess.application.ErrorType;
import jp.mts.authaccess.domain.model.UserType;
import jp.mts.authaccess.domain.model.social.SocialAuthProcess;
import jp.mts.authaccess.domain.model.social.SocialAuthProcessId;
import jp.mts.authaccess.domain.model.social.SocialAuthProvider;
import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.base.application.ApplicationException;
import jp.mts.base.util.Assertions;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

abstract class OAuth2AuthProvider implements SocialAuthProvider {

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
	
	protected JSONObject doUnirest(OAuth2AuthProvider.UnirestExecutor executor) {
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