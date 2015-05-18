package jp.mts.authaccess.application;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.NEW;

import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthRepository;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

public class AuthServiceTest {
	
	@Mocked AuthRepository authRepository;
	
	@Test
	public void test(){
		AuthService target = new AuthService();
		Deencapsulation.setField(target, authRepository);
		
		new Expectations() {{
			authRepository.authOf("hoge", "pass");
				result = new Auth("hoge", "taro");
		}};
		
		Auth auth = target.authenticate("hoge", "pass");
		assertThat(auth.id(), is("hoge"));
		assertThat(auth.name(), is("taro"));
	}
}
