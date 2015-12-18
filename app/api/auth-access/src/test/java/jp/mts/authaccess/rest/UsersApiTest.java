package jp.mts.authaccess.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jp.mts.authaccess.application.UserAppService;
import jp.mts.authaccess.domain.model.proper.ProperUserFixture;
import jp.mts.authaccess.rest.presentation.model.UserSave;
import jp.mts.base.rest.RestResponse;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;
import org.springframework.validation.BindingResult;

public class UsersApiTest {
	
	@Mocked UserAppService userService;
	@Mocked BindingResult bindingResult;

	@Test
	public void test_register_successful() {

		UsersApi target = new UsersApi();
		Deencapsulation.setField(target, userService);
		new Expectations() {{
			userService.register("u01", "taro@hoge.jp", "タスク太郎", "pass");
				result = new ProperUserFixture("u01").get();
		}};
		
		UserSave request = new UserSave();
		request.setUserId("u01");
		request.setEmail("taro@hoge.jp");
		request.setUserName("タスク太郎");
		request.setPassword("pass");
		request.setConfirmPassword("pass");

		RestResponse<UserSave> response = target.register(request, bindingResult);
		assertThat(response.getData().getUserId(), is("u01"));
	}
}
