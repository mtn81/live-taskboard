package jp.mts.taskmanage.application;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.GroupRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.junit.Test;

public class GroupAppServiceTest {

	@Tested GroupAppService target = new GroupAppService();
	@Injectable GroupRepository groupRepository;

	@Test
	public void test() {
		new Expectations() {{
			groupRepository.newGroupId();
				result = new GroupId("g01");
				
			groupRepository.save((Group)any);
		}};

		Group group = target.register("group01", "this is a test group");
		
		assertThat(group.groupId().value(), is("g01"));
		assertThat(group.name(), is("group01"));
		assertThat(group.description(), is("this is a test group"));
	}

}
