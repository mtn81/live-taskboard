package jp.mts.authaccess.infrastructure.persistence.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserBuilder;

import org.junit.Test;

public class JdbcUserRepositoryTest extends RepositoryTestBase {

	@Test
	public void test_persistence() {
		JdbcUserRepository target = new JdbcUserRepository();

		User user = new UserBuilder().build();
		target.save(user);

		User found = target.findById(user.id());
		assertThat(user.id(), is(found.id()));
	}
}
