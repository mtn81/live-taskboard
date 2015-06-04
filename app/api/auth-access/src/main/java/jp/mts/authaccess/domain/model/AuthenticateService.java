package jp.mts.authaccess.domain.model;

public class AuthenticateService {

	private UserRepository userRepository;
	private PasswordEncriptionService userPasswordEncriptionService;

	public AuthenticateService(UserRepository userRepository,
			PasswordEncriptionService userPasswordEncriptionService) {
		this.userRepository = userRepository;
		this.userPasswordEncriptionService = userPasswordEncriptionService;
	}

	public Auth authenticate(String id, String password) {
		UserId userId = new UserId(id);
		String encriptedPassword = userPasswordEncriptionService.encrypt(userId, password);
		User user = userRepository.findByAuthCredential(new UserId(id), encriptedPassword);
		if(user == null) return null;
		return new Auth(user.id(), user.name());
	}

}
