package jp.mts.authaccess.domain.model;


public interface UserRepository {

	User findById(UserId userId);
	void save(User aUser);

}
