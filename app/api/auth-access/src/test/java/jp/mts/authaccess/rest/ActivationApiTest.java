package jp.mts.authaccess.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.application.UserAppService;
import jp.mts.authaccess.domain.model.UserFixture;
import jp.mts.authaccess.rest.presentation.model.UserActivation;
import jp.mts.base.rest.RestResponse;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class ActivationApiTest {

	@Tested ActivationApi target = new ActivationApi();
	@Injectable UserAppService userAppService;

	@Test
	public void test_activateUser() {
		
		new Expectations() {{
			userAppService.activateUser("activate01");
				result = new UserFixture("user01").get();
		}};
		
		UserActivation userActivation = new UserActivation();;
		userActivation.setActivationId("activate01");

		RestResponse<UserActivation> response = target.activateUser(userActivation);
		
		assertThat(response.getData().getUserId(), is("user01"));
	}

}
