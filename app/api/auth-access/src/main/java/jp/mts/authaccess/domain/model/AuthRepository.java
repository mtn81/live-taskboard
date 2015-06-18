package jp.mts.authaccess.domain.model;

public interface AuthRepository {
	
	AuthId newAuthId();
	Auth findById(AuthId authId);
	void save(Auth auth);
}
