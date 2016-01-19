package jp.mts.taskmanage.rest;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import jp.mts.base.rest.RestResponse;
import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.application.query.GroupBelongingSearchQuery;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery;
import jp.mts.taskmanage.domain.model.GroupFixture;
import jp.mts.taskmanage.rest.presentation.model.GroupList;
import jp.mts.taskmanage.rest.presentation.model.GroupList.GroupView;
import jp.mts.taskmanage.rest.presentation.model.GroupRemove;
import jp.mts.taskmanage.rest.presentation.model.GroupSave;
import jp.mts.taskmanage.rest.presentation.model.GroupSearch;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.junit.Test;

import com.google.common.collect.Lists;

public class GroupApiTest {

	@Tested GroupApi target = new GroupApi();
	@Injectable GroupAppService groupAppService;
	@Injectable GroupJoinSearchQuery groupJoinSearchQuery;
	@Injectable GroupBelongingSearchQuery groupBelongingSearchQuery;

	@Test
	public void test_register() {
		new Expectations() {{
			groupAppService.registerGroup("member01", "group01", "this is a test group");
				result = new GroupFixture().get();
		}};
		
		GroupSave group = new GroupSave();
		group.setName("group01");
		group.setDescription("this is a test group");
		RestResponse<GroupSave> response = target.createGroupOnMember("member01", group);
		
		assertThat(response.getData().getGroupId(), is("g01"));
	}

	@Test
	public void test_list_belonging_groups() {
		target.initialize();
		
		new Expectations() {{
			groupBelongingSearchQuery.byMember("m01");
				result = newArrayList(
						new GroupBelongingSearchQuery.ByMemberResult("g01", "group01", "", true),
						new GroupBelongingSearchQuery.ByMemberResult("g02", "group02", "", false));
		}};

		RestResponse<GroupList> response = target.listBelongingGroups("m01");

		List<GroupView> groups = response.getData().getGroups();
		assertThat(groups.size(), is(2));
		assertThat(groups.get(0).getGroupId(), is("g01"));
		assertThat(groups.get(0).getGroupName(), is("group01"));
		assertThat(groups.get(0).isAdmin(), is(true));
		assertThat(groups.get(1).getGroupId(), is("g02"));
		assertThat(groups.get(1).getGroupName(), is("group02"));
		assertThat(groups.get(1).isAdmin(), is(false));
	}
	
	@Test
	public void test_remove_group() {
		String memberId = "m01";
		String groupId = "g01";
	
		new Expectations() {{
			groupAppService.removeGroup(memberId, groupId);
		}};

		RestResponse<GroupRemove> response = target.removeGroupOnMember(memberId, groupId);
		
		GroupRemove groupRemove = response.getData();
		assertThat(groupRemove, is(notNullValue()));
	}
	
	@Test
	public void test_searchGroupsByName() {
		target.initialize();

		new Expectations() {{
			groupJoinSearchQuery.notJoinAppliedWithName("m01", "group1");
				result = Lists.newArrayList(
						new GroupJoinSearchQuery.NotJoinAppliedWithNameResult("g01","group1","taro"),
						new GroupJoinSearchQuery.NotJoinAppliedWithNameResult("g02","group2","jiro"));
		}};
		
		RestResponse<GroupSearch> response = target.searchNotAppliedGroups("m01", "group1");
		
		List<GroupSearch.GroupView> groups = response.getData().getGroups();
		assertThat(groups.size(), is(2));
		assertThat(groups.get(0).getGroupId(), is("g01"));
		assertThat(groups.get(0).getGroupName(), is("group1"));
		assertThat(groups.get(0).getOwner(), is("taro"));
		assertThat(groups.get(1).getGroupId(), is("g02"));
		assertThat(groups.get(1).getGroupName(), is("group2"));
		assertThat(groups.get(1).getOwner(), is("jiro"));
	}
	
}
