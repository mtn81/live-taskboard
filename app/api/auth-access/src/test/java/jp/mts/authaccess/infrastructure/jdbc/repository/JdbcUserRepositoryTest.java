package jp.mts.authaccess.infrastructure.jdbc.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserActivationFixture;
import jp.mts.authaccess.domain.model.UserActivationId;
import jp.mts.authaccess.domain.model.UserFixture;
import jp.mts.authaccess.domain.model.UserStatus;
import jp.mts.base.unittest.JdbcTestBase;
import jp.mts.libs.unittest.Dates;

import org.junit.Before;
import org.junit.Test;

public class JdbcUserRepositoryTest extends JdbcTestBase {

	JdbcUserRepository target;

	@Before
	public void setup(){
		target = new JdbcUserRepository();
	}
	
	@Test
	public void test_persistence() {

		User user = new UserFixture("u01")
			.setEmail("hoge@foo.jp")
			.setEncryptedPassword("pass")
			.setName("taro")
			.setStatus(UserStatus.ACTIVE)
			.setUserActivation(new UserActivationFixture("a01")
				.setExpireTime(Dates.dateTime("2015/10/01 12:01:02.003"))
				.get())
			.get();

		target.save(user);

		User found = target.findById(user.id());
		assertThat(found, is(user));
		assertThat(found.id().value(), is("u01"));
		assertThat(found.email(), is("hoge@foo.jp"));
		assertThat(found.name(), is("taro"));
		assertThat(found.status(), is(UserStatus.ACTIVE));
		assertThat(found.userActivation().id().value(), is("a01"));
		assertThat(found.userActivation().expireTime(), is(Dates.dateTime("2015/10/01 12:01:02.003")));

	}
	
	@Test
	public void test_findByAuthCredential(){

		User user = new UserFixture().get();
		target.save(user);

		User found = target.findByAuthCredential(user.id(), user.encryptedPassword());
		assertThat(found, is(user));
	}
	
	@Test
	public void test_findByActivationId() {
		
		User user = new UserFixture()
			.setUserActivation(new UserActivationFixture("a01").get())
			.get();
		target.save(user);
		
		User found = target.findByActivationId(new UserActivationId("a01"));
		assertThat(found, is(user));

	}
}
