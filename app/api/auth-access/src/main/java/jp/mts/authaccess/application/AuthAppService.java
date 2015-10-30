package jp.mts.authaccess.application;

import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthId;
import jp.mts.authaccess.domain.model.AuthRepository;
import jp.mts.authaccess.domain.model.AuthenticateService;
import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserId;
import jp.mts.authaccess.domain.model.UserRepository;
import jp.mts.base.application.ApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthAppService {

	@Autowired
	private AuthenticateService authenticateService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthRepository authRepository;

	
	public void authenticate(String aUserId, String password, AuthenticateCallback callback) {
		UserId userId = new UserId(aUserId);
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
	
	@FunctionalInterface
	public interface AuthenticateCallback {
		void execute(Auth auth, User user);
	}

}
