package jp.mts.authaccess.domain.model.proper;

import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthId;
import jp.mts.authaccess.domain.model.AuthRepository;
import jp.mts.authaccess.domain.model.UserAuthenticated;
import jp.mts.base.domain.model.DomainEventPublisher;

public class ProperAuthenticateService {

	private ProperUserRepository userRepository;
	private AuthRepository authRepository;
	private PasswordEncriptionService passwordEncriptionService;
	private DomainEventPublisher domainEventPublisher;

	public ProperAuthenticateService(
			ProperUserRepository userRepository,
			AuthRepository authRepository,
			PasswordEncriptionService passwordEncriptionService,
			DomainEventPublisher domainEventPublisher) {
		this.userRepository = userRepository;
		this.authRepository = authRepository;
		this.passwordEncriptionService = passwordEncriptionService;
		this.domainEventPublisher = domainEventPublisher;
	}

	public Auth authenticate(ProperUserId userId, String password) {
		String encriptedPassword = passwordEncriptionService.encrypt(userId, password);
		ProperUser user = userRepository.findByAuthCredential(userId, encriptedPassword);
		if(user == null) return null;
		if(user.status() != ProperUserStatus.ACTIVE) return null;

		AuthId authId = authRepository.newAuthId();
		Auth auth = new Auth(authId, userId);

		domainEventPublisher.publish(new UserAuthenticated(authId, userId));
		authRepository.save(auth);
		return auth;
	}
	
	public ProperUser createUser(
			String id, 
			String email, 
			String name,
			String password){

		ProperUserId userId = new ProperUserId(id);
		ProperUser newUser = new ProperUser(
				userId, 
				email, 
				passwordEncriptionService.encrypt(userId, password), 
				name);
		
		domainEventPublisher.publish(
				new ProperUserRegistered(userId, newUser.userActivation().id(), name, email));
		
		return newUser;
	}
	
}
