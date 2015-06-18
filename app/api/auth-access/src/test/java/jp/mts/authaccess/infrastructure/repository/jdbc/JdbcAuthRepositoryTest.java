package jp.mts.authaccess.infrastructure.repository.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthFixture;

import org.junit.Test;

public class JdbcAuthRepositoryTest extends RepositoryTestBase {

	@Test
	public void test_persistence() {
		JdbcAuthRepository target = new JdbcAuthRepository();
		Auth auth = new AuthFixture()
			.authId(target.newAuthId()).get();

		target.save(auth);
		Auth found = target.findById(auth.id());
		
		assertThat(found, is(auth));
	}

}
