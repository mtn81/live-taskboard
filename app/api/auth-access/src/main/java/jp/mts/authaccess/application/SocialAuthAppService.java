package jp.mts.authaccess.application;

import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthRepository;
import jp.mts.authaccess.domain.model.social.SocialAuthDomainService;
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
			String acceptClientUrl,
			String rejectClientUrl) {
		String state = socialAuthDomainService.generateStateToken();

		SocialAuthProcess authProcess = new SocialAuthProcess(
				socialAuthProcessRepository.newAuthProcessId(), 
				state,
				socialAuthDomainService.authLocation(state),
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

	public SocialAuthProcess acceptAuthProcess(
			String processId,
			String stateToken,
			String authCode) {
		
		SocialAuthProcess socialAuthProcess = socialAuthProcessRepository.findById(new SocialAuthProcessId(processId)).get();
		if (!socialAuthProcess.stateToken().equals(stateToken)) {
			throw new ApplicationException(ErrorType.SOCIAL_AUTH_FAILED);
		}
		
		SocialUser socialUser = socialAuthDomainService.loadSocialUser(authCode);
		if (socialUser == null) {
			throw new ApplicationException(ErrorType.SOCIAL_AUTH_FAILED);
		}
		
		socialAuthProcess.associateUser(
				socialUser, !socialUserRepository.exists(socialUser.id()));

		socialUserRepository.save(socialUser);
		socialAuthProcessRepository.save(socialAuthProcess);
		
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
}
