package jp.mts.authaccess.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.rest.presentation.model.SocialUserLoad;
import jp.mts.authaccess.rest.presentation.model.SocialUserSave;
import jp.mts.base.rest.RestResponse;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

public class SocialUserApiTest {
	
	SocialUserApi target = new SocialUserApi();

	@Test public void 
	test_loadSocialUser(
		@Mocked SocialUserLoad socialUserLoad) {

		new Expectations() {{
			socialUserLoad.load("u01");
		}};
		
		RestResponse<SocialUserLoad> response = target.loadSocialUser("u01");
		
		assertThat(response.getData(), is(notNullValue()));
	}
	
	@Test public void
	test_saveSocialUser() {
		
		SocialUserSave socialUserSave = new SocialUserSave();
		new Expectations(socialUserSave) {{
			socialUserSave.save("u01"); 
		}};

		RestResponse<SocialUserSave> response = target.save("u01", socialUserSave);

		assertThat(response.getData(), is(sameInstance(socialUserSave)));
	}

}
