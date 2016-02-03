package jp.mts.authaccess.infrastructure.jdbc.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.domain.model.proper.ProperUser;
import jp.mts.authaccess.domain.model.proper.ProperUserActivationId;
import jp.mts.authaccess.domain.model.proper.ProperUserStatus;
import jp.mts.authaccess.domain.model.proper.ProperUserActivationFixture;
import jp.mts.authaccess.domain.model.proper.ProperUserFixture;
import jp.mts.base.unittest.JdbcTestBase;
import jp.mts.libs.unittest.Dates;

import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JdbcUserRepositoryTest extends JdbcTestBase {

	JdbcProperUserRepository target;

	@Before
	public void setup(){
		target = new JdbcProperUserRepository();
	}
	
	@Test
	public void test_persistence() {

		ProperUser user = new ProperUserFixture("u01")
			.setEmail("hoge@foo.jp")
			.setEncryptedPassword("pass")
			.setName("taro")
			.setStatus(ProperUserStatus.ACTIVE)
			.setUserActivation(new ProperUserActivationFixture("a01")
				.setExpireTime(Dates.dateTime("2015/10/01 12:01:02.003"))
				.get())
			.get();

		target.save(user);

		ProperUser found = target.findById(user.id());
		assertThat(found, is(user));
		assertThat(found.id().value(), is("u01"));
		assertThat(found.email(), is("hoge@foo.jp"));
		assertThat(found.name(), is("taro"));
		assertThat(found.status(), is(ProperUserStatus.ACTIVE));
		assertThat(found.userActivation().id().value(), is("a01"));
		assertThat(found.userActivation().expireTime(), is(Dates.dateTime("2015/10/01 12:01:02.003")));

	}
	
	@Test
	public void test_findByAuthCredential(){

		ProperUser user = new ProperUserFixture().get();
		target.save(user);

		ProperUser found = target.findByAuthCredential(user.id(), user.encryptedPassword());
		assertThat(found, is(user));
	}
	
	@Test
	public void test_findByActivationId() {
		
		ProperUser user = new ProperUserFixture()
			.setUserActivation(new ProperUserActivationFixture("a01").get())
			.get();
		target.save(user);
		
		ProperUser found = target.findByActivationId(new ProperUserActivationId("a01"));
		assertThat(found, is(user));

	}
}
