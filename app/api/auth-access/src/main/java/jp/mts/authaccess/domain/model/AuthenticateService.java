package jp.mts.authaccess.domain.model;

public class AuthenticateService {

	private UserRepository userRepository;
	private PasswordEncriptionService passwordEncriptionService;

	public AuthenticateService(UserRepository userRepository,
			PasswordEncriptionService passwordEncriptionService) {
		this.userRepository = userRepository;
		this.passwordEncriptionService = passwordEncriptionService;
	}

	public Auth authenticate(String id, String password) {
		UserId userId = new UserId(id);
		String encriptedPassword = passwordEncriptionService.encrypt(userId, password);
		User user = userRepository.findByAuthCredential(new UserId(id), encriptedPassword);
		if(user == null) return null;
		return new Auth(user.id(), user.name());
	}
	
	public User createUser(String id, String password, String email, String name){
		UserId userId = new UserId(id);
		return new User(userId, email, 
				passwordEncriptionService.encrypt(userId, password), name);
	}
	
	public void changePassword(User user, String password){
	}

}
