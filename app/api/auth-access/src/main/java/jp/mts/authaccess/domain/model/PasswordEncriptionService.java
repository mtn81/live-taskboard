package jp.mts.authaccess.domain.model;

public interface PasswordEncriptionService {

	String encrypt(UserId userId, String plainPassword);

}
