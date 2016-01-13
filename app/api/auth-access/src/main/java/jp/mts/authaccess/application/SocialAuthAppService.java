package jp.mts.authaccess.application;

import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthRepository;
import jp.mts.authaccess.domain.model.UserType;
import jp.mts.authaccess.domain.model.social.SocialAuthDomainService;
import jp.mts.authaccess.domain.model.social.SocialAuthDomainService.SocialAuthProvider;
import jp.mts.authaccess.domain.model.social.SocialAuthProcess;
import jp.mts.authaccess.domain.model.social.SocialAuthProcessId;
import jp.mts.authaccess.domain.model.social.SocialAuthProcessRepository;
import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.authaccess.domain.model.social.SocialUserRepository;
import jp.mts.base.application.ApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocialAuthAppService {
	
	@Autowired
	private SocialAuthProcessRepository socialAuthProcessRepository;
	@Autowired
	private SocialAuthDomainService socialAuthDomainService;
	@Autowired
	private SocialUserRepository socialUserRepository;
	@Autowired
	private AuthRepository authRepository;

	public SocialAuthProcess issueAuthProcess(
			UserType userType,
			String acceptClientUrl,
			String rejectClientUrl) {

		SocialAuthProvider provider = socialAuthDomainService.providerOf(userType);
		
		SocialAuthProcess authProcess = provider.startAuthProcess(
				socialAuthProcessRepository.newAuthProcessId(), 
				acceptClientUrl,
				rejectClientUrl);

		socialAuthProcessRepository.save(authProcess);
		
		return authProcess;
	}

	public SocialAuthProcess rejectAuthProcess(String processId) {
		 SocialAuthProcess socialAuthProcess = socialAuthProcessRepository.findById(new SocialAuthProcessId(processId)).get();
		 socialAuthProcessRepository.remove(socialAuthProcess);
		 return socialAuthProcess;
	}

	public SocialAuthProcess acceptOAuth2Process(
			String processId,
			String stateToken,
			String authCode) {
		
		SocialAuthProcess socialAuthProcess = socialAuthProcessRepository.findById(new SocialAuthProcessId(processId)).get();
		if (!socialAuthProcess.stateToken().equals(stateToken)) {
			throw new ApplicationException(ErrorType.SOCIAL_AUTH_FAILED);
		}
		attachSocialUserToProcess(authCode, stateToken, socialAuthProcess);
		return socialAuthProcess;
	}
	public SocialAuthProcess acceptOAuth1Process(
			String processId, String oAuthToken, String oAuthVerifier) {
		
		SocialAuthProcess socialAuthProcess = socialAuthProcessRepository.findById(new SocialAuthProcessId(processId)).get();
		attachSocialUserToProcess(oAuthVerifier, oAuthToken, socialAuthProcess);
		return socialAuthProcess;
	}


	public void confirmAuth(String processId, AuthenticateCallback callback) {
		
		SocialAuthProcess socialAuthProcess 
			= socialAuthProcessRepository.findById(new SocialAuthProcessId(processId)).get();
		
		SocialUser socialUser = socialUserRepository.findById(socialAuthProcess.socialUserId()).get();
		Auth auth = socialUser.createAuth(authRepository.newAuthId());

		authRepository.save(auth);
		socialAuthProcessRepository.remove(socialAuthProcess);
		
		callback.execute(auth, socialUser);
	}
	
	@FunctionalInterface
	public interface AuthenticateCallback {
		void execute(Auth auth, SocialUser user);
	}
	
	
	private void attachSocialUserToProcess(
			String authCode, String stateToken, SocialAuthProcess socialAuthProcess) {

		SocialUser socialUser = socialAuthDomainService.providerOf(socialAuthProcess.userType())
				.loadSocialUser(authCode, stateToken);
		if (socialUser == null) {
			throw new ApplicationException(ErrorType.SOCIAL_AUTH_FAILED);
		}
		
		socialAuthProcess.associateUser(
				socialUser, !socialUserRepository.exists(socialUser.id()));

		socialUserRepository.save(socialUser);
		socialAuthProcessRepository.save(socialAuthProcess);
		
	}
}
