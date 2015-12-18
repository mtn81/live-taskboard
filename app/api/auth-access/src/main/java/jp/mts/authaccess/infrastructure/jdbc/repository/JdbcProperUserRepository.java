package jp.mts.authaccess.infrastructure.jdbc.repository;

import java.util.Date;

import jp.mts.authaccess.domain.model.proper.ProperUser;
import jp.mts.authaccess.domain.model.proper.ProperUserActivation;
import jp.mts.authaccess.domain.model.proper.ProperUserActivationId;
import jp.mts.authaccess.domain.model.proper.ProperUserBuilder;
import jp.mts.authaccess.domain.model.proper.ProperUserId;
import jp.mts.authaccess.domain.model.proper.ProperUserRepository;
import jp.mts.authaccess.domain.model.proper.ProperUserStatus;
import jp.mts.authaccess.infrastructure.jdbc.model.UserModel;

import org.springframework.stereotype.Repository;

@Repository
public class JdbcProperUserRepository implements ProperUserRepository {

	@Override
	public void save(ProperUser aUser) {
		UserModel userModel = UserModel.findFirst("user_id = ?", aUser.id().value());
		if(userModel == null){
			userModel = new UserModel();
		}
		userModel.set(
			"user_id", aUser.id().value(),
			"email", aUser.email(),
			"name", aUser.name(),
			"password", aUser.encryptedPassword(),
			"status", aUser.status().name(),
			"activation_id", aUser.userActivation().id().value());
		userModel.setTimestamp(
			"activation_expire", aUser.userActivation().expireTime());
		userModel.saveIt();
	}

	@Override
	public ProperUser findById(ProperUserId userId) {
		UserModel userModel = UserModel.findFirst("user_id = ?", userId.value());
		return fromDbToDomain(userModel);
	}

	@Override
	public ProperUser findByAuthCredential(ProperUserId userId, String encryptedPassword) {
		UserModel userModel = UserModel.findFirst("user_id = ? and password = ?", userId.value(), encryptedPassword);
		return fromDbToDomain(userModel);
	}
	
	@Override
	public ProperUser findByActivationId(ProperUserActivationId userActivationId) {
		UserModel userModel = UserModel.findFirst("activation_id = ?", userActivationId.value());
		return fromDbToDomain(userModel);
	}
	
	private ProperUser fromDbToDomain(UserModel userModel){
		if(userModel == null) return null;

		return new ProperUserBuilder(
			new ProperUser(
				new ProperUserId(userModel.getString("user_id")), 
				userModel.getString("email"), 
				userModel.getString("password"),
				userModel.getString("name")))
			.setStatus(ProperUserStatus.valueOf(userModel.getString("status")))
			.setUserActivation(
				new ProperUserActivation(
					new ProperUserActivationId(userModel.getString("activation_id")), 
					new Date(userModel.getTimestamp("activation_expire").getTime())))
			.get();
	}
	
}
