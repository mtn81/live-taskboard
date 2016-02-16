package jp.mts.authaccess.domain.model.social;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class SocialUserTest {

	@Test
	public void test_email() {
		SocialUser socialUser = new SocialUserFixture().get();
		assertThat(socialUser.email(), is("social@test.jp"));

		SocialUser socialUser2 = new SocialUserFixture().setEmail("social2@test.jp").get();
		assertThat(socialUser2.email(), is("social2@test.jp"));
	}
	@Test
	public void test_name() {
		SocialUser socialUser = new SocialUserFixture().get();
		assertThat(socialUser.name(), is("taro"));

		SocialUser socialUser2 = new SocialUserFixture().setName("taro2").get();
		assertThat(socialUser2.name(), is("taro2"));
	}

}
