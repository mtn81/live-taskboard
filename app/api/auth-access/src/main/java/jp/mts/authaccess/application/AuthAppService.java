package jp.mts.authaccess.application;

import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthenticateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthAppService {

	@Autowired
	private AuthenticateService authenticateService;

	public Auth authenticate(String id, String password) {
		Auth auth = authenticateService.authenticate(id, password);
		if(auth == null) 
			throw new ApplicationException(ErrorType.AUTH_FAILED);		
		return auth;
	}
}
