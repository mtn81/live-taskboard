package jp.mts.authaccess.application;

import org.springframework.stereotype.Service;

import jp.mts.authaccess.domain.model.Auth;

@Service
public class AuthService {

	public Auth authenticate(String id, String password) {
		return new Auth("hoge", "タスク太郎");
	}
}
