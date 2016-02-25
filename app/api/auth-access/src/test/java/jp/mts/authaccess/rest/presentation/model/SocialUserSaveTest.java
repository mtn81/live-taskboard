package jp.mts.authaccess.rest.presentation.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.application.SocialUserAppService;
import jp.mts.authaccess.domain.model.social.SocialUserFixture;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Before;
import org.junit.Test;

public class SocialUserSaveTest {
	
	@Mocked SocialUserAppService socialUserAppService;
	
	@Before
	public void setup() {
		SocialUserSave.setSocialUserAppService(socialUserAppService);
	}

	@Test public void 
	test_save() {
		SocialUserSave target = new SocialUserSave();
		target.setUserName("taro");
		target.setEmail("hoge@test.jp");
		target.setNotifyEmail(true);
		
		new Expectations() {{
			socialUserAppService.changeUserAttributes("GOOGLE_u01", "taro", "hoge@test.jp", true);
				result = new SocialUserFixture().get();
		}};

		target.save("GOOGLE_u01");
		
		assertThat(target.getUserId(), is("GOOGLE_u01"));
	}

}
