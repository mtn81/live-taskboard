package jp.mts.authaccess.domain.model;

public interface UserActivationRepository {

	UserActivation findById(UserActivationId userActivationId);
	void save(UserActivation userActivationPromise);
	void remove(UserActivation userActivationPromise);
	UserActivationId newActivationId();

}
