package jp.mts.authaccess.infrastructure.persistence.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.infrastructure.persistence.jdbc.model.UserModel;

import org.junit.Before;
import org.junit.Test;

public class JdbcAuthRepositoryTest extends RepositoryTestBase {
	
	JdbcAuthRepository target;

	@Before
	public void setup(){
		UserModel.createIt(
            "user_id",  "hoge", 
            "password", "pass", 
            "name",     "taro");
		
		target = new JdbcAuthRepository();
	}
	
	@Test
	public void test(){
		Auth auth = target.authOf("hoge", "pass");
		
		assertThat(auth.id(), is("hoge"));
		assertThat(auth.name(), is("taro"));
	}
	
	@Test
	public void test_null(){
		assertThat(target.authOf("foo", "pass"), is(nullValue()));
	}
}
