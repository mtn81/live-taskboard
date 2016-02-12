package jp.mts.authaccess.infrastructure.jdbc.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jp.mts.authaccess.domain.model.UserType;
import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.authaccess.domain.model.social.SocialUserFixture;
import jp.mts.authaccess.domain.model.social.SocialUserId;
import jp.mts.base.unittest.JdbcTestBase;

import org.junit.Test;

public class JdbcSocialUserRepositoryTest extends JdbcTestBase {
	
	JdbcSocialUserRepository target = new JdbcSocialUserRepository();

	@Test
	public void test_persistence() {
		SocialUserId socialUserId = new SocialUserId(UserType.GOOGLE, "u01");

		target.save(
			new SocialUserFixture(
					socialUserId, "test@test.jp", "taro")
				.setEmail("test2@test.jp")
				.setName("taro2")
				.setUseEmailNotification(true)
				.get());
		
		SocialUser actual = target.findById(socialUserId).get();
		
		assertThat(actual.id(), is(socialUserId));
		assertThat(actual.originalName(), is("taro"));
		assertThat(actual.name(), is("taro2"));
		assertThat(actual.originalEmail(), is("test@test.jp"));
		assertThat(actual.email(), is("test2@test.jp"));
		assertThat(actual.useEmailNotification(), is(true));

	}

}
