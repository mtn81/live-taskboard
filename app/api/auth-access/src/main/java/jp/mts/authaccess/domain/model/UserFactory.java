package jp.mts.authaccess.domain.model;

public class UserFactory {
	
	private PasswordEncriptionService passwordEncriptionService;
	
	public UserFactory(
			PasswordEncriptionService passwordEncriptionService) {
		this.passwordEncriptionService = passwordEncriptionService;
	}

	public User create(UserId userId, String email, String name, String password){
		User user = new User(userId, email, 
				passwordEncriptionService.encrypt(userId, password), name);
		return user;
	}
}
