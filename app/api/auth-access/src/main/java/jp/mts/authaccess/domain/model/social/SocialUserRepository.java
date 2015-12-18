package jp.mts.authaccess.domain.model.social;

import java.util.Optional;

public interface SocialUserRepository {

	Optional<SocialUser> findById(SocialUserId socialUserId);
	boolean exists(SocialUserId socialUserId);
	void save(SocialUser socialUser);
}
