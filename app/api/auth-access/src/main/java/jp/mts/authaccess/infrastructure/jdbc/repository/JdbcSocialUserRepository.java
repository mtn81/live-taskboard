package jp.mts.authaccess.infrastructure.jdbc.repository;

import java.util.Optional;

import jp.mts.authaccess.domain.model.UserType;
import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.authaccess.domain.model.social.SocialUserId;
import jp.mts.authaccess.domain.model.social.SocialUserRepository;
import jp.mts.authaccess.infrastructure.jdbc.model.SocialUserModel;

import org.springframework.stereotype.Repository;

@Repository
public class JdbcSocialUserRepository implements SocialUserRepository {
	
	@Override
	public boolean exists(SocialUserId socialUserId) {
		return findById(socialUserId).isPresent();
	}

	@Override
	public Optional<SocialUser> findById(SocialUserId socialUserId) {
		SocialUserModel model = SocialUserModel.findFirst("user_id=?", socialUserId.idValue());
		if (model == null) return Optional.empty();
		return Optional.of(
			new SocialUser(
				new SocialUserId(
						UserType.valueOf(model.getString("type")), 
						model.getString("social_id")), 
				model.getString("emal"), 
				model.getString("name"))
		);
	}

	@Override
	public void save(SocialUser socialUser) {
		SocialUserModel model = SocialUserModel.findFirst("user_id=?", socialUser.id().idValue());
		if (model == null) {
			model = new SocialUserModel();
		}
		
		model.set(
				"user_id", socialUser.id().idValue(),
				"email", socialUser.email(),
				"name", socialUser.name(),
				"social_id", socialUser.id().socialId(),
				"type", socialUser.id().userType().name())
			.saveIt();
	}


}
