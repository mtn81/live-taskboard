package jp.mts.authaccess.infrastructure.jdbc.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.domain.model.UserActivationPromise;
import jp.mts.authaccess.domain.model.UserActivationPromiseFixture;
import jp.mts.base.unittest.JdbcTestBase;

import org.junit.Test;

public class JdbcUserActivationRepositoryTest extends JdbcTestBase{

	JdbcUserActivationRepository target = new JdbcUserActivationRepository();
	
	@Test
	public void test_persistence() {
		
		UserActivationPromise userActivation = new UserActivationPromiseFixture().get();
		target.save(userActivation);
		
		UserActivationPromise found = target.findById(userActivation.id());
		
		assertThat(found, is(userActivation));
		assertThat(found.id(), is(userActivation.id()));
		assertThat(found.userId(), is(userActivation.userId()));
		assertThat(found.expireTime(), is(userActivation.expireTime()));
	}

}
