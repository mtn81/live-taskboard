package jp.mts.authaccess.infrastructure.jdbc.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.authaccess.domain.model.social.SocialUserId;
import jp.mts.authaccess.domain.model.social.SocialUserRepository;

@Repository
public class JdbcSocialUserRepository implements SocialUserRepository {

	private Map<SocialUserId, SocialUser> map = new HashMap<>();
	
	@Override
	public Optional<SocialUser> findById(SocialUserId socialUserId) {
		return map.containsKey(socialUserId) ? Optional.of(map.get(socialUserId)) : Optional.empty();
	}

	@Override
	public boolean exists(SocialUserId socialUserId) {
		return findById(socialUserId).isPresent();
	}

	@Override
	public void save(SocialUser socialUser) {
		map.put(socialUser.id(), socialUser);
	}

}
