package jp.mts.authaccess.infrastructure.jdbc.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.infrastructure.jdbc.model.UserModel;
import jp.mts.authaccess.test.helper.JdbcTestBase;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import org.junit.Test;

public class RepositoryBasicTest extends JdbcTestBase {
	
	@Test
	public void test_transactional_1(){
		Example.createIt("name", "foo");
		assertThat(UserModel.findFirst("name = ?", "bar"), is(nullValue())); ;
	}
	@Test
	public void test_transactional_2(){
		Example.createIt("name", "bar");
		assertThat(UserModel.findFirst("name = ?", "foo"), is(nullValue())); ;
	}
	
	@Table("examples")
	public static class Example extends Model { }
}
