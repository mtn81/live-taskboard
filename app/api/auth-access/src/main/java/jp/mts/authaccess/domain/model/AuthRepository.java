package jp.mts.authaccess.domain.model;

public interface AuthRepository {
	
	Auth authOf(String id, String password);

}
