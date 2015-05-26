package jp.mts.authaccess.infrastructure.persistence.jdbc;

import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserId;
import jp.mts.authaccess.domain.model.UserRepository;
import jp.mts.authaccess.infrastructure.persistence.jdbc.model.UserModel;

import org.javalite.activejdbc.DB;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserRepository implements UserRepository {

	@Override
	public void save(User aUser) {
		UserModel userModel = UserModel.findFirst("user_id = ?", aUser.id().value());
		if(userModel == null){
			userModel = new UserModel();
		}
		userModel.set(
			"user_id",  aUser.id().value(),
			"email",    aUser.email(),
			"name",     aUser.name(),
			"password", aUser.password());
		userModel.saveIt();
	}

	@Override
	public User findById(UserId userId) {
		UserModel userModel = UserModel.findFirst("user_id = ?", userId.value());
		if(userModel == null){
			return null;
		}
		
		User user = new User(new UserId(userModel.getString("user_id")));
		
		return user;
	}
	
}
