package jp.mts.taskmanage.rest;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import jp.mts.base.rest.RestResponse;
import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.application.GroupAppService.GroupBelongingPair;
import jp.mts.taskmanage.application.query.GroupSearchQuery;
import jp.mts.taskmanage.domain.model.GroupBelongingFixture;
import jp.mts.taskmanage.domain.model.GroupFixture;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.GroupJoinApplication;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationFixture;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationId;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.rest.presentation.model.GroupJoinApply;
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
	@Injectable GroupSearchQuery groupSearchQuery;

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
		new Expectations() {{
			groupAppService.listGroupBelongingFor("m01");
				result = newArrayList(
						new GroupBelongingPair(new GroupFixture("g01").get(), new GroupBelongingFixture("g01", "m01").get()), 
						new GroupBelongingPair(new GroupFixture("g02").get(), new GroupBelongingFixture("g02", "m01").get()));
		}};

		RestResponse<GroupList> response = target.listBelongingGroups("m01");

		List<GroupView> groups = response.getData().getGroups();
		assertThat(groups.size(), is(2));
		assertThat(groups.get(0).getGroupId(), is("g01"));
		assertThat(groups.get(1).getGroupId(), is("g02"));
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
		new Expectations() {{
			groupSearchQuery.byName("group1");
				result = Lists.newArrayList(
						new GroupSearchQuery.Result("g01","group1","taro"),
						new GroupSearchQuery.Result("g02","group2","jiro"));
		}};
		
		RestResponse<GroupSearch> response = target.searchGroups("group1");
		
		List<GroupSearch.GroupView> groups = response.getData().getGroups();
		assertThat(groups.size(), is(2));
		assertThat(groups.get(0).getGroupId(), is("g01"));
		assertThat(groups.get(0).getGroupName(), is("group1"));
		assertThat(groups.get(0).getOwner(), is("taro"));
		assertThat(groups.get(1).getGroupId(), is("g02"));
		assertThat(groups.get(1).getGroupName(), is("group2"));
		assertThat(groups.get(1).getOwner(), is("jiro"));
	}
	
	@Test
	public void test_applyJoin() {
		
		String groupId = "g01";
		String applicantMemberId = "m01";
		String applicationId = "a01";
		
		new Expectations() {{
			groupAppService.applyJoin(groupId, applicantMemberId);
				result = new GroupJoinApplicationFixture(groupId,applicantMemberId).get();
		}};
		
		GroupJoinApply groupJoinApply = new GroupJoinApply();
		groupJoinApply.setApplicantMemberid(applicantMemberId);
		RestResponse<GroupJoinApply> response = target.applyJoin(groupId, groupJoinApply);
		
		assertThat(response.getData().getJoinId(), is(applicationId));
	}
}
