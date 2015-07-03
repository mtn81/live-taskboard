package jp.mts.taskmanage.infrastructure.repository.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.GroupFixture;

import org.junit.Test;

public class JdbcGroupRepositoryTest extends JdbcRepositoryTestBase {
	
	JdbcGroupRepository groupRepository = new JdbcGroupRepository();
	
	@Test
	public void test_UUID表示() {
		JdbcGroupRepository target = new JdbcGroupRepository();
		System.out.println(
			target.newGroupId().value()
		);
	}

	@Test
	public void test_persistence() {
		Group group = new GroupFixture().get();
		groupRepository.save(group);
		
		Group found = groupRepository.findById(group.groupId());
		assertThat(group, is(found));
		
		found.changeAttributes("group_changed_name", "group changed");
		groupRepository.save(found);

		found = groupRepository.findById(group.groupId());
		assertThat(found.name(), is("group_changed_name"));
	}
}
