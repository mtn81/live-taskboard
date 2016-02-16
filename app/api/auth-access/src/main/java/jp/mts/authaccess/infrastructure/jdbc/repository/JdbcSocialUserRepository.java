package jp.mts.authaccess.infrastructure.jdbc.repository;

import java.util.Optional;

import jp.mts.authaccess.domain.model.UserType;
import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.authaccess.domain.model.social.SocialUserBuilder;
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
			new SocialUserBuilder(
				new SocialUser(
					new SocialUserId(
							UserType.valueOf(model.getString("type")), 
							model.getString("social_id")), 
					model.getString("orig_email"), 
					model.getString("orig_name")))
				.setName(model.getString("name"))
				.setEmail(model.getString("email"))
				.setUseEmailNotification(model.getBoolean("notify_email"))
				.get()
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
				"orig_name", socialUser.originalName(),
				"orig_email", socialUser.originalEmail(),
				"email", socialUser.email(),
				"name", socialUser.name(),
				"notify_email", socialUser.useEmailNotification(),
				"social_id", socialUser.id().socialId(),
				"type", socialUser.id().userType().name())
			.saveIt();
	}


}
