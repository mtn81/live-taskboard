package jp.mts.authaccess.domain.model;


public interface UserRepository {

	User findById(UserId userId);
	User findByAuthCredential(UserId userId, String string);
	void save(User aUser);

}
