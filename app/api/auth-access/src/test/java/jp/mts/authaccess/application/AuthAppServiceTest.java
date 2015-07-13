package jp.mts.authaccess.application;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import jp.mts.authaccess.application.AuthAppService.AuthResult;
import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthFixture;
import jp.mts.authaccess.domain.model.AuthenticateService;
import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserFixture;
import jp.mts.authaccess.domain.model.UserId;
import jp.mts.authaccess.domain.model.UserRepository;
import jp.mts.base.application.ApplicationException;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

public class AuthAppServiceTest {
	
	@Mocked AuthenticateService authenticateService;
	@Mocked UserRepository userRepository;
	
	@Test
	public void test_認証情報が取得できる(){
		AuthAppService target = new AuthAppService();
		Deencapsulation.setField(target, authenticateService);
		Deencapsulation.setField(target, userRepository);
		
		Auth auth = new AuthFixture().get();
		User user = new UserFixture().get();
		UserId userId = new UserId("hoge");
		new Expectations() {{
			authenticateService.authenticate(userId, "pass");
				result = auth;
			userRepository.findById(userId);
				result = user;
		}};
		
		AuthResult actual = target.authenticate("hoge", "pass");
		assertThat(actual.auth, is(auth));
		assertThat(actual.user, is(user));
	}
	
	@Test
	public void test_認証情報が取得できない場合エラー(){
		
		AuthAppService target = new AuthAppService();
		Deencapsulation.setField(target, authenticateService);
		
		new Expectations() {{
			authenticateService.authenticate(new UserId("hoge"), "pass");
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
