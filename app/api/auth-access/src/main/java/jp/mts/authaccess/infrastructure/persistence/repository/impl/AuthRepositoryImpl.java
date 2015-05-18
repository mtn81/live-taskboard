package jp.mts.authaccess.infrastructure.persistence.repository.impl;

import org.springframework.stereotype.Repository;

import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthRepository;

@Repository
public class AuthRepositoryImpl implements AuthRepository {

	@Override
	public Auth authOf(String id, String password){
		return new Auth("hoge", "test taro");
	}
}
