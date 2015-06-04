package jp.mts.authaccess.rest;

import static jp.mts.authaccess.test.helper.RestResponseMatcher.hasError;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.servlet.http.HttpServletResponse;

import jp.mts.authaccess.application.ApplicationException;
import jp.mts.authaccess.application.AuthAppService;
import jp.mts.authaccess.application.ErrorType;
import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.UserId;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;

public class AuthApiTest {
	
	@Mocked AuthAppService authAppService;
	@Mocked HttpServletResponse mockHttpServletResponse;

	AuthApi target;

	@Before
	public void setup(){
		target = new AuthApi();
		Deencapsulation.setField(target, authAppService);
	}
	
	@Test
	public void test_認証が成功する(){
		
		new NonStrictExpectations() {{
			authAppService.authenticate("hoge", "pass");
				result = new Auth(new UserId("hoge"), "taro");
		}};
		AuthenticateRequest request = new AuthenticateRequest();
		request.id = "hoge";
		request.password = "pass";

		RestResponse<AuthView> response = target.authenticate(request, mockHttpServletResponse);
		AuthView authView = response.getData();
		
		assertThat(authView.getId(), is("hoge"));
		assertThat(authView.getUserName(), is("taro"));
	}

	@Test
	public void test_認証が失敗する(){
		new Expectations() {{
			authAppService.authenticate("hoge", "pass");
				result = new ApplicationException(ErrorType.AUTH_FAILED);
			mockHttpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}};
		AuthenticateRequest request = new AuthenticateRequest();
		request.id = "hoge";
		request.password = "pass";

		RestResponse<AuthView> response = target.authenticate(request, mockHttpServletResponse);
		
		assertThat(response, hasError("e001"));
	}
}
