package jp.mts.authaccess.rest.presentation.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jp.mts.authaccess.application.SocialAuthAppService;
import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.authaccess.domain.model.social.SocialUserFixture;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;

import org.junit.Before;
import org.junit.Test;

public class SocialUserLoadTest {
	
	@Tested SocialUserLoad target = new SocialUserLoad();
	@Mocked SocialAuthAppService socialAuthAppService;
	
	@Before
	public void setup() {
		SocialUserLoad.setSocialAuthAppService(socialAuthAppService);
	}

	@Test
	public void test_load() {
		SocialUser socialUser = new SocialUserFixture()
			.setName("taro").setEmail("test@test.jp").get();

		new Expectations() {{
			socialAuthAppService.loadSocialUserInProcess("p01");
				result = socialUser;
		}};
		
		target.load("p01");
		
		assertThat(target.getUserName(), is("taro"));
		assertThat(target.getEmail(), is("test@test.jp"));
	}

}
