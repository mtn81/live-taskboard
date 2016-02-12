package jp.mts.authaccess.rest.presentation.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jp.mts.authaccess.application.SocialUserAppService;
import jp.mts.authaccess.domain.model.social.SocialUser;
import jp.mts.authaccess.domain.model.social.SocialUserFixture;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;

import org.junit.Before;
import org.junit.Test;

public class SocialUserLoadTest {

	@Tested SocialUserLoad target = new SocialUserLoad();
	@Mocked SocialUserAppService socialUserAppService;
	
	@Before
	public void setup() {
		SocialUserLoad.setSocialAppService(socialUserAppService);
	}
	
	@Test
	public void test_load() {
		
		SocialUser socialUser = new SocialUserFixture()
			.setName("taro")
			.setOriginalName("ori_taro")
			.setEmail("social@test.jp")
			.setOriginalEmail("ori_social@test.jp")
			.setUseEmailNotification(true)
			.get();

		new Expectations(){{
			socialUserAppService.loadUser("u01");
				result = socialUser;
		}};
		
		target.load("u01");
		
		assertThat(target.getName(), is("taro"));
		assertThat(target.getEmail(), is("social@test.jp"));
		assertThat(target.getOriginalName(), is("ori_taro"));
		assertThat(target.getOriginalEmail(), is("ori_social@test.jp"));
		assertThat(target.useEmailNotification(), is(true));
		
	}

}
