package jp.mts.taskmanage.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.domain.model.GroupFixture;
import jp.mts.taskmanage.rest.presentation.model.GroupSave;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Before;
import org.junit.Test;

public class GroupsApiTest {

	GroupsApi target;
	@Mocked GroupAppService groupAppService;
	
	@Before
	public void setup(){
		target = new GroupsApi();
		Deencapsulation.setField(target, groupAppService);
	}

	@Test
	public void test() {
		new Expectations() {{
			groupAppService.register("group01", "this is a test group");
				result = new GroupFixture().get();
		}};
		
		GroupSave group = new GroupSave();
		group.setName("group01");
		group.setDescription("this is a test group");
		RestResponse<GroupSave> response = target.create(group);
		
		assertThat(response.getData().getGroupId(), is("g01"));
	}

}
