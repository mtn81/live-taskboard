package jp.mts.taskmanage.infrastructure.jdbc.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;

import jp.mts.base.unittest.JdbcTestBase;
import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.GroupFixture;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupMemberModel;

import org.junit.Test;

public class JdbcGroupRepositoryTest extends JdbcTestBase {
	
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
		
		Group found = groupRepository.findById(group.groupId()).get();
		assertThat(group, is(found));
		
		found.changeAttributes("group_changed_name", "group changed");
		groupRepository.save(found);

		found = groupRepository.findById(group.groupId()).get();
		assertThat(found.name(), is("group_changed_name"));
	}
	
	@Test
	public void test_remove() {
		
		//setup
		Group group = new GroupFixture("g01a").get();
		groupRepository.save(group);
		
		GroupMemberModel.createIt(
				"group_id", "g01a",
				"member_id", "m01a",
				"admin", true);
		
		//exec
		groupRepository.remove(group);
		
		//assert
		Optional<Group> foundGroup = groupRepository.findById(new GroupId("g01a"));
		assertThat(foundGroup.isPresent(), is(false));
		assertThat(GroupMemberModel.findFirst("group_id=?", "g01a"), is(nullValue()));
	}
}
