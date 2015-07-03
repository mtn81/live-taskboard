package jp.mts.authaccess.application;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.notNull;
import jp.mts.authaccess.domain.model.AuthenticateService;
import jp.mts.authaccess.domain.model.DomainEventPublisher;
import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserFixture;
import jp.mts.authaccess.domain.model.UserId;
import jp.mts.authaccess.domain.model.UserRegistered;
import jp.mts.authaccess.domain.model.UserRepository;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;

import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.prism.impl.Disposer.Target;

public class UserAppServiceTest {

	@Tested UserAppService target = new UserAppService();
	@Injectable UserRepository userRepository;
	@Injectable AuthenticateService authenticateService;

	@Test
	public void test_登録処理() {

		final User user = new UserFixture().get();
		new Expectations() {{
			authenticateService.createUser("u01", "taro@hoge.jp", "タスク太郎", "pass");
				result = user;
			userRepository.save(user);
		}};
		
		User actual = target.register("u01", "taro@hoge.jp", "タスク太郎", "pass");
		
		assertThat(actual, is(user));
	}

}
