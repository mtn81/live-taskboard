package jp.mts.authaccess.infrastructure.persistence.jdbc;

import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthRepository;
import jp.mts.authaccess.infrastructure.persistence.jdbc.model.UserModel;

import org.springframework.stereotype.Repository;

@Repository
public class JdbcAuthRepository implements AuthRepository {
	
	@Override
	public Auth authOf(String id, String password){
		UserModel user = UserModel.findFirst("user_id = ? and password = ?", id, password);
		if(user == null) return null;
		return new Auth(user.getString("user_id"), user.getString("name"));
	}
}
