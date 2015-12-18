package jp.mts.authaccess.domain.model.proper;


public interface PasswordEncriptionService {

	String encrypt(ProperUserId userId, String plainPassword);

}
