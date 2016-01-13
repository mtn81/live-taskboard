package jp.mts.authaccess.rest.presentation.model;

import jp.mts.authaccess.application.SocialAuthAppService;
import jp.mts.authaccess.domain.model.social.SocialAuthProcess;

public class SocialAuthAccept {

	//required services
	private static SocialAuthAppService socialAuthAppService;
	
	public static void setSocialAuthAppService(
			SocialAuthAppService socialAuthAppService) {
		SocialAuthAccept.socialAuthAppService = socialAuthAppService;
	}
	
	//output
	private SocialAuthProcess socialAuthProcess;
	
	public String getClientUrl() {
		return socialAuthProcess.acceptClientUrl();
	}
	public boolean isFirstUse() {
		return socialAuthProcess.firstUse();
	}
	
	public void acceptOAuth2(
			String processId, 
			String stateToken,
			String authCode) {
		
		socialAuthProcess 
			= socialAuthAppService.acceptOAuth2Process(processId, stateToken, authCode);
		
	}
	public void acceptOAuth1(
			String processId, 
			String oAuthToken,
			String oAuthVerifier) {
		
		socialAuthProcess 
			= socialAuthAppService.acceptOAuth1Process(processId, oAuthToken, oAuthVerifier);
		
	}

}
