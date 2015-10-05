package jp.mts.authaccess.application;

import jp.mts.authaccess.domain.model.AuthenticateService;
import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserId;
import jp.mts.authaccess.domain.model.UserRepository;
import jp.mts.base.application.ApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserAppService {
	
	@Autowired UserRepository userRepository;
	@Autowired AuthenticateService authenticateService;

	public User register(
			String userId, 
			String email, 
			String name, 
			String password) {
		
		validateForRegister(userId);
		
		User newUser = authenticateService.createUser(
				userId, email, name, password);
		
		userRepository.save(newUser);
		return newUser; 
	}

	public void validateForRegister(String userId) {
		User user = userRepository.findById(new UserId(userId));
		if(user != null) 
			throw new ApplicationException(ErrorType.USER_ID_ALREADY_EXISTS);
	}

}
