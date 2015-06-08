package jp.mts.authaccess.application;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthFixture;
import jp.mts.authaccess.domain.model.AuthenticateService;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

public class AuthAppServiceTest {
	
	@Mocked AuthenticateService authenticateService;
	
	@Test
	public void test_認証情報が取得できる(){
		AuthAppService target = new AuthAppService();
		Deencapsulation.setField(target, authenticateService);
		
		Auth auth = new AuthFixture().build();
		new Expectations() {{
			authenticateService.authenticate("hoge", "pass");
				result = auth;
		}};
		
		Auth actual = target.authenticate("hoge", "pass");
		assertThat(actual, is(auth));
	}
	
	@Test
	public void test_認証情報が取得できない場合エラー(){
		
		AuthAppService target = new AuthAppService();
		Deencapsulation.setField(target, authenticateService);
		
		new Expectations() {{
			authenticateService.authenticate("hoge", "pass");
				result = null;
		}};
		
		try{
			target.authenticate("hoge", "pass");
			fail();
		}catch(ApplicationException e){
			assertThat(e.hasErrorOf(ErrorType.AUTH_FAILED), is(true));
		}
	}
}
