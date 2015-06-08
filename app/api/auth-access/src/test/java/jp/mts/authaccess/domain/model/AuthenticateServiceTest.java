package jp.mts.authaccess.domain.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class AuthenticateServiceTest {

	@Mocked	UserRepository userRepository;
	@Mocked PasswordEncriptionService userPasswordEncriptinService;
	
	@Test
	public void test() {
		AuthenticateService target = new AuthenticateService(userRepository, userPasswordEncriptinService);
		
		new Expectations() {{
			userPasswordEncriptinService.encrypt(new UserId("u01"), "pass");
				result = "encryptedPass";
			userRepository.findByAuthCredential(new UserId("u01"), "encryptedPass");
				result = new UserFixture().build();
		}};
		Auth auth = target.authenticate("u01", "pass");
		
		assertThat(auth, is(new Auth(new UserId("u01"), "タスク太郎")));
	}

}
