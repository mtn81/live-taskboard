package jp.mts.authaccess.domain.model;

public interface User {
	
	UserId userId();
	String name();
	String email();
	boolean useEmailNotification();
}
