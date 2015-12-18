package jp.mts.authaccess.rest.presentation.model;

import org.hibernate.validator.constraints.NotBlank;

import jp.mts.authaccess.application.SocialAuthAppService;
import jp.mts.authaccess.domain.model.social.SocialAuthProcess;

public class SocialAuthStart {

	//required services
	private static SocialAuthAppService socialAuthAppService;
	
	public static void setSocialAuthAppService(
			SocialAuthAppService socialAuthAppService) {
		SocialAuthStart.socialAuthAppService = socialAuthAppService;
	}
	
	//input
	@NotBlank
	private String acceptClientUrl;
	@NotBlank
	private String rejectClientUrl;

	public void setAcceptClientUrl(String acceptClientUrl) {
		this.acceptClientUrl = acceptClientUrl;
	}
	public void setRejectClientUrl(String rejectClientUrl) {
		this.rejectClientUrl = rejectClientUrl;
	}

	//output
	private SocialAuthProcess socialAuthProcess;
	
	public String getProcessId() {
		return socialAuthProcess.id().value();
	}
	public String getState() {
		return socialAuthProcess.stateToken();
	}
	public String getAuthLocation() {
		return socialAuthProcess.authLocation();
	}
	
	//process
	public void start() {
		socialAuthProcess = socialAuthAppService.issueAuthProcess(
				acceptClientUrl, rejectClientUrl);
	}
}
