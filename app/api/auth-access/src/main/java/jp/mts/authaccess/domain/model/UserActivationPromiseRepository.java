package jp.mts.authaccess.domain.model;

public interface UserActivationPromiseRepository {

	UserActivationPromise findById(UserActivationId userActivationId);
	void save(UserActivationPromise userActivationPromise);

}
