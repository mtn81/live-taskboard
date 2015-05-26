package jp.mts.authaccess.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserFactory;
import jp.mts.authaccess.domain.model.UserId;
import jp.mts.authaccess.domain.model.UserRepository;

@Service
public class UserService {
	
	@Autowired UserRepository userRepository;

	public User register(
			String userIdValue, 
			String email, 
			String name, 
			String password) {

		User newUser = new UserFactory().create(
				new UserId(userIdValue), email, name, password);
		
		userRepository.save(newUser);
		return newUser; 
	}

}
