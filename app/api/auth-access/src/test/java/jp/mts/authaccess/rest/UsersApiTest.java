package jp.mts.authaccess.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jp.mts.authaccess.application.UserAppService;
import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserFixture;
import jp.mts.authaccess.domain.model.UserId;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class UsersApiTest {
	
	@Mocked UserAppService userService;

	@Test
	public void test_register_successful() {

		UsersApi target = new UsersApi();
		Deencapsulation.setField(target, userService);
		new Expectations() {{
			userService.register("u01", "taro@hoge.jp", "タスク太郎", "pass");
				result = new UserFixture().get();
		}};
		
		UserRegisterRequest request = new UserRegisterRequest();
		request.userId = "u01";
		request.email = "taro@hoge.jp";
		request.name = "タスク太郎";
		request.password = "pass";
		request.confirmPassword= "pass";

		RestResponse<UserRegisterView> response = target.register(request);
		assertThat(response.getData().getId(), is("u01"));
	}
}
