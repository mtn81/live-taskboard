package jp.mts.authaccess.rest.presentation.model;

import jp.mts.authaccess.application.SocialAuthAppService;
import jp.mts.authaccess.domain.model.social.SocialAuthProcess;

public class SocialAuthReject {

	//required services
	private static SocialAuthAppService socialAuthAppService;
	
	public static void setSocialAuthAppService(
			SocialAuthAppService socialAuthAppService) {
		SocialAuthReject.socialAuthAppService = socialAuthAppService;
	}
	
	//output
	private SocialAuthProcess socialAuthProcess;
	
	public String getClientUrl() {
		return socialAuthProcess.rejectClientUrl();
	}
	
	public void reject(String processId) {
		socialAuthProcess 
			= socialAuthAppService.rejectAuthProcess(processId);
	}

}
