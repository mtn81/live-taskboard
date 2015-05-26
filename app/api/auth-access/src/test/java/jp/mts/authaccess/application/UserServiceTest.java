package jp.mts.authaccess.application;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserRepository;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

public class UserServiceTest {

	@Mocked UserRepository userRepository;

	@Test
	public void test() {
		UserService target = new UserService();
		Deencapsulation.setField(target, userRepository);
		new Expectations() {{
			userRepository.save((User)any);
		}};
		
		User user = target.register("u01", "taro@hoge.jp", "タスク太郎", "pass");
		
		assertThat(user.id().value(), is("u01"));
		assertThat(user.email(), is("taro@hoge.jp"));
		assertThat(user.name(), is("タスク太郎"));
		assertThat(user.password(), is("pass"));
	}

}
