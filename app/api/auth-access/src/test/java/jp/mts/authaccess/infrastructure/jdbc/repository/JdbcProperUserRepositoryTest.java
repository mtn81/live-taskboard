package jp.mts.authaccess.infrastructure.jdbc.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jp.mts.authaccess.domain.model.proper.ProperUser;
import jp.mts.authaccess.domain.model.proper.ProperUserActivationFixture;
import jp.mts.authaccess.domain.model.proper.ProperUserFixture;
import jp.mts.authaccess.domain.model.proper.ProperUserId;
import jp.mts.authaccess.domain.model.proper.ProperUserStatus;
import jp.mts.base.unittest.JdbcTestBase;
import jp.mts.libs.unittest.Dates;

import org.junit.Test;

public class JdbcProperUserRepositoryTest extends JdbcTestBase {
	
	JdbcProperUserRepository target = new JdbcProperUserRepository();

	@Test public void 
	test_persistence() {
		
		ProperUser user = new ProperUserFixture("u01")
			.setName("taro")
			.setEmail("taro@test.jp")
			.setEncryptedPassword("pass")
			.setStatus(ProperUserStatus.ACTIVE)
			.setUseEmailNotification(true)
			.setUserActivation(
				new ProperUserActivationFixture("a01")
					.setExpireTime(Dates.dateShortTime("2016/02/01 12:00"))
					.get())
			.get();
		
		target.save(user);

		ProperUser found = target.findById(new ProperUserId("u01"));
		
		assertThat(found.id().value(), is("u01"));
		assertThat(found.name(), is("taro"));
		assertThat(found.email(), is("taro@test.jp"));
		assertThat(found.encryptedPassword(), is("pass"));
		assertThat(found.status(), is(ProperUserStatus.ACTIVE));
		assertThat(found.useEmailNotification(), is(true));
		assertThat(found.userActivation().id().value(), is("a01"));
		assertThat(found.userActivation().expireTime(), is(Dates.dateShortTime("2016/02/01 12:00")));

		
	}

}
