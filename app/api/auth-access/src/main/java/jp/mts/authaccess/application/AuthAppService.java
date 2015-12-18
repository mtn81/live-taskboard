package jp.mts.authaccess.application;

import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthId;
import jp.mts.authaccess.domain.model.AuthRepository;
import jp.mts.authaccess.domain.model.proper.ProperAuthenticateService;
import jp.mts.authaccess.domain.model.proper.ProperUser;
import jp.mts.authaccess.domain.model.proper.ProperUserId;
import jp.mts.authaccess.domain.model.proper.ProperUserRepository;
import jp.mts.base.application.ApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthAppService {

	@Autowired
	private ProperAuthenticateService authenticateService;
	@Autowired
	private ProperUserRepository userRepository;
	@Autowired
	private AuthRepository authRepository;

	
	public void authenticate(String aUserId, String password, AuthenticateCallback callback) {
		ProperUserId userId = new ProperUserId(aUserId);
		Auth auth = authenticateService.authenticate(userId, password);
		if(auth == null) 
			throw new ApplicationException(ErrorType.AUTH_FAILED);
		
		callback.execute(auth, userRepository.findById(userId));
	}

	public Auth load(String authId) {
		Auth auth = authRepository.findById(new AuthId(authId));
		if (auth == null) {
			throw new ApplicationException(ErrorType.AUTH_NOT_FOUND);
		}
		return auth;
	}

	public void remove(String authId) {
		Auth auth = authRepository.findById(new AuthId(authId));
		if (auth == null) {
			throw new ApplicationException(ErrorType.AUTH_NOT_FOUND);
		}
		authRepository.remove(auth);
	}
	
	@FunctionalInterface
	public interface AuthenticateCallback {
		void execute(Auth auth, ProperUser user);
	}


}
