package jp.mts.authaccess.rest;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import jp.mts.authaccess.application.SocialAuthAppService;
import jp.mts.authaccess.rest.presentation.model.SocialAuthAccept;
import jp.mts.authaccess.rest.presentation.model.SocialAuthConfirm;
import jp.mts.authaccess.rest.presentation.model.SocialAuthReject;
import jp.mts.authaccess.rest.presentation.model.SocialAuthStart;
import jp.mts.base.rest.RestResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api")
public class SocialAuthApi {

	@Autowired
	private SocialAuthAppService socialAuthAppService; 
	
	@PostConstruct
	public void initialize() {
		SocialAuthStart.setSocialAuthAppService(socialAuthAppService);
		SocialAuthReject.setSocialAuthAppService(socialAuthAppService);
		SocialAuthAccept.setSocialAuthAppService(socialAuthAppService);
		SocialAuthConfirm.setSocialAuthAppService(socialAuthAppService);
	}
	
	@RequestMapping(
		value="/social_auth/{socialSite}", 
		params="start",
		method=RequestMethod.POST)
	public RestResponse<SocialAuthStart> startAuth(
			@PathVariable String socialSite,
			@Valid @RequestBody SocialAuthStart socialAuthStart){
		socialAuthStart.start(socialSite);
		return RestResponse.of(socialAuthStart);
	}


	@RequestMapping(
		value="/social_auth/oauth1", 
		params={"accept", "!denied", "oauth_token", "oauth_verifier"},
		method=RequestMethod.GET)
	public ModelAndView acceptAuthInOAuth1(
			@CookieValue("auth_pid") String processId,
			@RequestParam("oauth_token") String oAuthToken,
			@RequestParam("oauth_verifier") String oAuthVerifier,
			HttpServletRequest request) throws IOException{
		
		SocialAuthAccept socialAuthAccept = new SocialAuthAccept();
		socialAuthAccept.acceptOAuth1(processId, oAuthToken, oAuthVerifier);
		return new ModelAndView("redirect:" 
				+ socialAuthAccept.getClientUrl() 
				+ "?first_use=" + socialAuthAccept.isFirstUse());
	}
	@RequestMapping(
		value="/social_auth/oauth1", 
		params={"accept", "denied"},
		method=RequestMethod.GET)
	public ModelAndView rejectAuthInOAuth1(
			@CookieValue("auth_pid") String processId,
			HttpServletRequest request) throws IOException{

		SocialAuthReject socialAuthReject = new SocialAuthReject();
		socialAuthReject.reject(processId);
		
		return new ModelAndView("redirect:" 
				+ socialAuthReject.getClientUrl());
	}

	@RequestMapping(
		value="/social_auth", 
		params={"accept", "!error", "state", "code"},
		method=RequestMethod.GET)
	public ModelAndView acceptAuth(
			@CookieValue("auth_pid") String processId,
			@RequestParam("state") String stateToken,
			@RequestParam("code") String authCode,
			HttpServletRequest request) throws IOException{

		SocialAuthAccept socialAuthAccept = new SocialAuthAccept();
		socialAuthAccept.acceptOAuth2(processId, stateToken, authCode);
		return new ModelAndView("redirect:" 
				+ socialAuthAccept.getClientUrl() 
				+ "?first_use=" + socialAuthAccept.isFirstUse());
	}
	@RequestMapping(
		value="/social_auth", 
		params={"accept", "error"},
		method=RequestMethod.GET)
	public ModelAndView rejectAuth(
			@CookieValue("auth_pid") String processId,
			HttpServletRequest request) throws IOException{
		
		SocialAuthReject socialAuthReject = new SocialAuthReject();
		socialAuthReject.reject(processId);
		
		return new ModelAndView("redirect:" 
				+ socialAuthReject.getClientUrl());
	}
	
	@RequestMapping(
		value="/social_auth", 
		params="confirm",
		method=RequestMethod.GET)
	public RestResponse<SocialAuthConfirm> confirmAuth(
			@CookieValue("auth_pid") String processId) {
		
		SocialAuthConfirm socialAuthConfirm = new SocialAuthConfirm();
		socialAuthConfirm.confirm(processId);
		return RestResponse.of(socialAuthConfirm);
	}
}
