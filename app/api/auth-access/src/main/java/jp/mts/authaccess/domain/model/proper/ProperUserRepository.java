package jp.mts.authaccess.domain.model.proper;


public interface ProperUserRepository {

	ProperUser findById(ProperUserId userId);
	ProperUser findByAuthCredential(ProperUserId userId, String string);
	void save(ProperUser aUser);
	ProperUser findByActivationId(ProperUserActivationId userActivationId);

}
