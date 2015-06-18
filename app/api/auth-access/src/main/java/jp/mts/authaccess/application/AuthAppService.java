package jp.mts.authaccess.application;

import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthenticateService;
import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserId;
import jp.mts.authaccess.domain.model.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthAppService {

	@Autowired
	private AuthenticateService authenticateService;
	@Autowired
	private UserRepository userRepository;

	
	public AuthResult authenticate(String aUserId, String password) {
		UserId userId = new UserId(aUserId);
		Auth auth = authenticateService.authenticate(userId, password);
		if(auth == null) 
			throw new ApplicationException(ErrorType.AUTH_FAILED);
		
		return new AuthResult(auth, userRepository.findById(userId));
	}
	
	
	public static class AuthResult {
		public Auth auth;
		public User user;
		
		public AuthResult(Auth auth, User user) {
			this.auth = auth;
			this.user = user;
		}
	}
}
