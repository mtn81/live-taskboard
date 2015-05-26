package jp.mts.authaccess.domain.model;

public class UserFactory {
	
	public User create(
			UserId userId, String email, String name, String password){
		User user = new User(userId);
		user.setEmail(email);
		user.setName(name);
		user.setPassword(password);
		return user;
	}
}
