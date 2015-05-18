package jp.mts.authaccess.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import jp.mts.authaccess.application.AuthService;
import jp.mts.authaccess.domain.model.Auth;
import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.NonStrictExpectations;

public class AuthApiTest {
	
	@Mocked AuthService authAppService;

	@Test
	public void test_認証が成功する(){
		AuthApi target = new AuthApi();
		Deencapsulation.setField(target, authAppService);
		
		new NonStrictExpectations() {{
			authAppService.authenticate("hoge", "pass");
				result = new Auth("hoge", "taro");
		}};
		AuthenticateRequest request = new AuthenticateRequest();
		request.id = "hoge";
		request.password = "pass";

		RestResponse<AuthView> response = target.authenticate(request);
		AuthView authView = response.getData();
		
		assertThat(authView.getId(), is("hoge"));
		assertThat(authView.getUserName(), is("taro"));
	}
}
