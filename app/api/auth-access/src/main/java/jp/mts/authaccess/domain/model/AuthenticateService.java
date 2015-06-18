package jp.mts.authaccess.domain.model;

public class AuthenticateService {

	private UserRepository userRepository;
	private AuthRepository authRepository;
	private PasswordEncriptionService passwordEncriptionService;

	public AuthenticateService(
			UserRepository userRepository,
			AuthRepository authRepository,
			PasswordEncriptionService passwordEncriptionService) {
		this.userRepository = userRepository;
		this.authRepository = authRepository;
		this.passwordEncriptionService = passwordEncriptionService;
	}

	public Auth authenticate(UserId userId, String password) {
		String encriptedPassword = passwordEncriptionService.encrypt(userId, password);
		User user = userRepository.findByAuthCredential(userId, encriptedPassword);
		if(user == null) return null;

		Auth auth = new Auth(authRepository.newAuthId(), userId);
		authRepository.save(auth);
		return auth;
	}
	
	public User createUser(String id, String password, String email, String name){
		UserId userId = new UserId(id);
		return new User(userId, email, 
				passwordEncriptionService.encrypt(userId, password), name);
	}
	
	public void changePassword(User user, String password){
	}

}
