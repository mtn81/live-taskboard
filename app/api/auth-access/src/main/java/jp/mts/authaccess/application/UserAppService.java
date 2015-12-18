package jp.mts.authaccess.application;

import jp.mts.authaccess.domain.model.proper.ProperAuthenticateService;
import jp.mts.authaccess.domain.model.proper.ProperUser;
import jp.mts.authaccess.domain.model.proper.ProperUserActivationId;
import jp.mts.authaccess.domain.model.proper.ProperUserId;
import jp.mts.authaccess.domain.model.proper.ProperUserRepository;
import jp.mts.authaccess.domain.model.proper.ProperUserStatus;
import jp.mts.base.application.ApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserAppService {
	
	@Autowired ProperUserRepository userRepository;
	@Autowired ProperAuthenticateService authenticateService;

	public ProperUser register(
			String userId, 
			String email, 
			String name, 
			String password) {
		
		validateForRegister(userId);
		
		ProperUser newUser = authenticateService.createUser(
				userId, email, name, password);
		
		userRepository.save(newUser);

		return newUser; 
	}

	public void validateForRegister(String userId) {
		ProperUser user = userRepository.findById(new ProperUserId(userId));
		if(user != null) 
			throw new ApplicationException(ErrorType.USER_ID_ALREADY_EXISTS);
	}

	public ProperUser activateUser(String activationId) {
		ProperUser user = userRepository.findByActivationId(new ProperUserActivationId(activationId));
		if(user == null) 
			throw new ApplicationException(ErrorType.ACTIVATION_NOT_FOUND);
		if(user.status() == ProperUserStatus.ACTIVE)
			throw new ApplicationException(ErrorType.ACTIVATION_NOT_FOUND);

		if (!user.activate()) {
			throw new ApplicationException(ErrorType.ACTIVATION_EXPIRED);
		}
		
		userRepository.save(user);
		return user;
	}

}
