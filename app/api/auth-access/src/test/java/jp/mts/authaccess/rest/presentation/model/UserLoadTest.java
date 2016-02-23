package jp.mts.authaccess.rest.presentation.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.application.UserAppService;
import jp.mts.authaccess.domain.model.proper.ProperUser;
import jp.mts.authaccess.domain.model.proper.ProperUserFixture;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Before;
import org.junit.Test;

public class UserLoadTest {
	
	UserLoad target = new UserLoad();
	@Mocked UserAppService userAppService;

	@Before
	public void setup() {
		UserLoad.setUserAppService(userAppService);
	}
	
	@Test public void 
	test_load() {
		
		ProperUser user = new ProperUserFixture()
			.setName("taro")
			.setEmail("taro@test.jp")
			.setUseEmailNotification(true)
			.get();

		new Expectations() {{
			userAppService.loadUserById("u01");
				result = user;
		}};

		target.load("u01");
		
		assertThat(target.getUserId(), is("u01"));
		assertThat(target.getName(), is("taro"));
		assertThat(target.getEmail(), is("taro@test.jp"));
		assertThat(target.isNotifyEmail(), is(true));
		
	}

}
