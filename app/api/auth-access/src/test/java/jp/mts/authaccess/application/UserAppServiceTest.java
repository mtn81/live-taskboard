package jp.mts.authaccess.application;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.notNull;
import jp.mts.authaccess.domain.model.AuthenticateService;
import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserFixture;
import jp.mts.authaccess.domain.model.UserId;
import jp.mts.authaccess.domain.model.UserRepository;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class UserAppServiceTest {

	@Mocked UserRepository userRepository;
	@Mocked AuthenticateService authenticateService;

	@Test
	public void test() {
		UserAppService target = new UserAppService();
		Deencapsulation.setField(target, userRepository);
		Deencapsulation.setField(target, authenticateService);

		final User user = new UserFixture().build();
		new Expectations() {{
			authenticateService.createUser("u01", "taro@hoge.jp", "タスク太郎", "pass");
				result = user;
			userRepository.save(user);
		}};
		
		User actual = target.register("u01", "taro@hoge.jp", "タスク太郎", "pass");
		
		assertThat(actual, is(user));
	}

}
