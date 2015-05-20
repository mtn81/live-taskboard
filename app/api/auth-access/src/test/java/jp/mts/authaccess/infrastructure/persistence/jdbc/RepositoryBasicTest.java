package jp.mts.authaccess.infrastructure.persistence.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.infrastructure.persistence.jdbc.model.UserModel;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import org.junit.Test;

public class RepositoryBasicTest extends RepositoryTestBase {
	
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
