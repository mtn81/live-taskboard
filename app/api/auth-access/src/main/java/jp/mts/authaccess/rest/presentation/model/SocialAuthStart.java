package jp.mts.authaccess.rest.presentation.model;

import org.hibernate.validator.constraints.NotBlank;

import jp.mts.authaccess.application.ErrorType;
import jp.mts.authaccess.application.SocialAuthAppService;
import jp.mts.authaccess.domain.model.UserType;
import jp.mts.authaccess.domain.model.social.SocialAuthProcess;
import jp.mts.base.application.ApplicationException;

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
	public void start(String socialSite) {
		socialAuthProcess = socialAuthAppService.issueAuthProcess(
				getUserType(socialSite), acceptClientUrl, rejectClientUrl);
	}

	private UserType getUserType(String socialSite) {
		if("google".equals(socialSite)) return UserType.GOOGLE;
		if("yahoo".equals(socialSite)) return UserType.YAHOO;
		if("facebook".equals(socialSite)) return UserType.FACEBOOK;
		if("twitter".equals(socialSite)) return UserType.TWITTER;

		throw new ApplicationException(ErrorType.ACTIVATION_EXPIRED);
	}
}
