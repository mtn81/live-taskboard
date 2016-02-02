package jp.mts.authaccess.application;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthFixture;
import jp.mts.authaccess.domain.model.proper.ProperAuthenticateService;
import jp.mts.authaccess.domain.model.proper.ProperUser;
import jp.mts.authaccess.domain.model.proper.ProperUserFixture;
import jp.mts.authaccess.domain.model.proper.ProperUserId;
import jp.mts.authaccess.domain.model.proper.ProperUserRepository;
import jp.mts.base.application.ApplicationException;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

public class AuthAppServiceTest {
	
	@Mocked ProperAuthenticateService authenticateService;
	@Mocked ProperUserRepository userRepository;
	
	@Test
	public void test_認証情報が取得できる(){
		AuthAppService target = new AuthAppService();
		Deencapsulation.setField(target, authenticateService);
		Deencapsulation.setField(target, userRepository);
		
		Auth auth = new AuthFixture().get();
		ProperUser user = new ProperUserFixture().get();
		ProperUserId userId = new ProperUserId("hoge");
		new Expectations() {{
			authenticateService.authenticate(userId, "pass");
				result = auth;
			userRepository.findById(userId);
				result = user;
		}};
		
		target.authenticate("hoge", "pass", (aAuth, aUser) -> {
			assertThat(aAuth, is(auth));
			assertThat(aUser, is(user));
		});
	}
	
	@Test
	public void test_認証情報が取得できない場合エラー(){
		
		AuthAppService target = new AuthAppService();
		Deencapsulation.setField(target, authenticateService);
		
		new Expectations() {{
			authenticateService.authenticate(new ProperUserId("hoge"), "pass");
				result = null;
		}};
		
		try{
			target.authenticate("hoge", "pass", (auth, user) -> {});
			fail();
		}catch(ApplicationException e){
			assertThat(e.hasErrorOf(ErrorType.AUTH_FAILED), is(true));
		}
	}
}
