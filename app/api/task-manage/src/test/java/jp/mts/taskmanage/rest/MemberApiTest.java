package jp.mts.taskmanage.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import jp.mts.base.rest.RestResponse;
import jp.mts.taskmanage.application.MemberAppService;
import jp.mts.taskmanage.domain.model.MemberFixture;
import jp.mts.taskmanage.rest.presentation.model.MemberList;
import jp.mts.taskmanage.rest.presentation.model.MemberList.MemberView;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.junit.Test;

import com.google.common.collect.Lists;

public class MemberApiTest {

	@Tested MemberApi target = new MemberApi();
	@Injectable MemberAppService memberAppService;
	
	@Test
	public void test() {
		
		new Expectations() {{
			memberAppService.findMembersInGroup("g01");
				result = Lists.newArrayList(
						new MemberFixture("m01", "taro").addGroupBelonging("g01", true).get(),
						new MemberFixture("m02", "jiro").addGroupBelonging("g01", false).get());
		}};
		
		RestResponse<MemberList> response = target.loadMembersInGroup("g01");

		List<MemberView> members = response.getData().getMembers();
		assertThat(members.size(), is(2));
		assertThat(members.get(0).getMemberId(), is("m01"));
		assertThat(members.get(0).getName(), is("taro"));
		assertThat(members.get(0).isAdmin(), is(true));
		assertThat(members.get(1).getMemberId(), is("m02"));
		assertThat(members.get(1).getName(), is("jiro"));
		assertThat(members.get(1).isAdmin(), is(false));
		
	}

}
