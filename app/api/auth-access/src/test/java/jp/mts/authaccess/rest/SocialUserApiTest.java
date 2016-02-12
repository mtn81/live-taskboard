package jp.mts.authaccess.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.rest.presentation.model.SocialUserLoad;
import jp.mts.base.rest.RestResponse;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;

import org.junit.Test;

public class SocialUserApiTest {
	
	@Tested SocialUserApi target = new SocialUserApi();

	@Test
	public void test_loadSocialUser(
			@Mocked SocialUserLoad socialUserLoad) {

		new Expectations() {{
			socialUserLoad.load("u01");
		}};
		
		RestResponse<SocialUserLoad> response = target.loadSocialUser("u01");
		
		assertThat(response.getData(), is(notNullValue()));
	}

}
