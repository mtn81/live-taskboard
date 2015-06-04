package jp.mts.authaccess.application;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthenticateService;
import jp.mts.authaccess.domain.model.UserId;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

public class AuthAppServiceTest {
	
	@Mocked AuthenticateService authenticateService;
	
	@Test
	public void test(){
		AuthAppService target = new AuthAppService();
		Deencapsulation.setField(target, authenticateService);
		
		new Expectations() {{
			authenticateService.authenticate("hoge", "pass");
				result = new Auth(new UserId("hoge"), "taro");
		}};
		
		Auth auth = target.authenticate("hoge", "pass");
		assertThat(auth.id(), is(new UserId("hoge")));
		assertThat(auth.name(), is("taro"));
	}
}
