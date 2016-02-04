package jp.mts.authaccess.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.notNull;
import jp.mts.authaccess.rest.presentation.model.SocialUserLoad;
import jp.mts.base.rest.RestResponse;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

public class SocialAuthApiTest {

	SocialAuthApi socialAuthApi = new SocialAuthApi();
	
	@Test
	public void test_loadUser(@Mocked SocialUserLoad SocialUserLoad) {
		new Expectations() {{
			SocialUserLoad socialUserLoad = new SocialUserLoad();
			socialUserLoad.load("p01");
		}};
		
		RestResponse<SocialUserLoad> response = socialAuthApi.loadUser("p01");
		
		assertThat(response.getData(), is(notNullValue()));
	}

}
