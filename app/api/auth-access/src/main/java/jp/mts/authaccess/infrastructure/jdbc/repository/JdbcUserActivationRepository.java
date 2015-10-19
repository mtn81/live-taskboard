package jp.mts.authaccess.infrastructure.jdbc.repository;

import static org.hamcrest.CoreMatchers.nullValue;
import jp.mts.authaccess.domain.model.UserActivationPromise;
import jp.mts.authaccess.domain.model.UserActivationId;
import jp.mts.authaccess.domain.model.UserActivationPromiseBuilder;
import jp.mts.authaccess.domain.model.UserActivationPromiseRepository;
import jp.mts.authaccess.domain.model.UserId;
import jp.mts.authaccess.infrastructure.jdbc.model.UserActivationModel;

public class JdbcUserActivationRepository implements UserActivationPromiseRepository{

	@Override
	public UserActivationPromise findById(UserActivationId userActivationId) {
		UserActivationModel model = findModel(userActivationId);
		if (model == null) {
			return null;
		}
		return new UserActivationPromise(
						new UserActivationId(model.getString("activation_id")),
						new UserId(model.getString("user_id")),
						model.getDate("expire"));
	}

	@Override
	public void save(UserActivationPromise userActivation) {
		UserActivationModel model = findModel(userActivation.id());
		if (model == null) {
			model = new UserActivationModel();
		}
		model.set(
				"activation_id", userActivation.id().value(),
				"user_id", userActivation.userId().value(),
				"expire", userActivation.expireTime())
			.saveIt();
		
	}

	private UserActivationModel findModel(UserActivationId userActivationId) {
		return UserActivationModel.findFirst("activation_id=?", userActivationId.value());
	}

}
