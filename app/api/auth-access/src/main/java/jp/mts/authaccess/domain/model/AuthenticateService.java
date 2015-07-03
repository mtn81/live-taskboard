package jp.mts.authaccess.domain.model;

public class AuthenticateService {

	private UserRepository userRepository;
	private AuthRepository authRepository;
	private PasswordEncriptionService passwordEncriptionService;
	private DomainEventPublisher domainEventPublisher;

	public AuthenticateService(
			UserRepository userRepository,
			AuthRepository authRepository,
			PasswordEncriptionService passwordEncriptionService,
			DomainEventPublisher domainEventPublisher) {
		this.userRepository = userRepository;
		this.authRepository = authRepository;
		this.passwordEncriptionService = passwordEncriptionService;
		this.domainEventPublisher = domainEventPublisher;
	}

	public Auth authenticate(UserId userId, String password) {
		String encriptedPassword = passwordEncriptionService.encrypt(userId, password);
		User user = userRepository.findByAuthCredential(userId, encriptedPassword);
		if(user == null) return null;

		AuthId authId = authRepository.newAuthId();
		Auth auth = new Auth(authId, userId);

		domainEventPublisher.publish(new UserAuthenticated(authId, userId));
		authRepository.save(auth);
		return auth;
	}
	
	public User createUser(String id, String password, String email, String name){
		UserId userId = new UserId(id);
		User newUser =  new User(userId, email, 
				passwordEncriptionService.encrypt(userId, password), name);
		return newUser;
	}
	
	public void changePassword(User user, String password){
	}

}
