package jp.mts.authaccess.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.authaccess.domain.model.social.SocialUserId;
import jp.mts.authaccess.domain.model.social.SocialUserRepository;

@Service
public class SocialUserAppService {

	@Autowired
	private SocialUserRepository socialUserRepository;
	
	public SocialUser loadUser(String userId) {
		return socialUserRepository.findById(
				SocialUserId.fromIdValue(userId)).get();
	}

}
