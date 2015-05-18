package jp.mts.authaccess.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthRepository;

@Service
public class AuthService {

	@Autowired
	private AuthRepository authRepository;

	public Auth authenticate(String id, String password) {
		return authRepository.authOf(id, password);
	}
}
