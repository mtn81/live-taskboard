package jp.mts.authaccess.application;

import jp.mts.authaccess.domain.model.AuthenticateService;
import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAppService {
	
	@Autowired UserRepository userRepository;
	@Autowired AuthenticateService authenticateService;

	public User register(
			String userId, 
			String email, 
			String name, 
			String password) {

		User newUser = authenticateService.createUser(
				userId, email, name, password);
		
		userRepository.save(newUser);
		return newUser; 
	}

}
