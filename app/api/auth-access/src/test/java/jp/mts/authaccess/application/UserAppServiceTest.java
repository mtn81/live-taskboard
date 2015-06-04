package jp.mts.authaccess.application;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserFactory;
import jp.mts.authaccess.domain.model.UserId;
import jp.mts.authaccess.domain.model.UserRepository;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class UserAppServiceTest {

	@Mocked UserRepository userRepository;
	@Mocked UserFactory userFactory;

	@Test
	public void test() {
		UserAppService target = new UserAppService();
		Deencapsulation.setField(target, userRepository);
		Deencapsulation.setField(target, userFactory);

		new Expectations() {{
			userRepository.save((User)any);
			userFactory.create(new UserId("u01"), "taro@hoge.jp", "タスク太郎", "pass");
				result = new User(new UserId("u01"), "taro@hoge.jp", "pass", "タスク太郎");
		}};
		
		User user = target.register("u01", "taro@hoge.jp", "タスク太郎", "pass");
		
		assertThat(user.id().value(), is("u01"));
		assertThat(user.email(), is("taro@hoge.jp"));
		assertThat(user.name(), is("タスク太郎"));
		assertThat(user.encryptedPassword(), is("pass"));
	}

}
