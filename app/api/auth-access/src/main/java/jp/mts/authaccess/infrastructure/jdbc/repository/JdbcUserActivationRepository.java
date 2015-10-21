package jp.mts.authaccess.infrastructure.jdbc.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import jp.mts.authaccess.domain.model.UserActivationId;
import jp.mts.authaccess.domain.model.UserActivation;
import jp.mts.authaccess.domain.model.UserActivationRepository;
import jp.mts.authaccess.domain.model.UserId;
import jp.mts.authaccess.infrastructure.jdbc.model.UserActivationModel;

@Repository
public class JdbcUserActivationRepository implements UserActivationRepository{

	@Override
	public UserActivation findById(UserActivationId userActivationId) {
		UserActivationModel model = findModel(userActivationId);
		if (model == null) {
			return null;
		}
		return new UserActivation(
						new UserActivationId(model.getString("activation_id")),
						new UserId(model.getString("user_id")),
						model.getDate("expire"));
	}

	@Override
	public void save(UserActivation userActivation) {
		UserActivationModel model = findModel(userActivation.id());
		if (model == null) {
			model = new UserActivationModel();
		}
		model.set(
				"activation_id", userActivation.id().value(),
				"user_id", userActivation.userId().value());
		model.setDate(
				"expire", userActivation.expireTime());
		model.saveIt();
	}
	
	@Override
	public void remove(UserActivation userActivationPromise) {
		UserActivationModel.delete(
				"activation_id=?", userActivationPromise.id().value());
	}

	@Override
	public UserActivationId newActivationId() {
		return new UserActivationId(UUID.randomUUID().toString());
	}

	private UserActivationModel findModel(UserActivationId userActivationId) {
		return UserActivationModel.findFirst("activation_id=?", userActivationId.value());
	}


}
