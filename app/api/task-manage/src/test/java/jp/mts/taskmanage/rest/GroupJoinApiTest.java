package jp.mts.taskmanage.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.base.rest.RestResponse;
import jp.mts.taskmanage.application.GroupJoinAppService;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationFixture;
import jp.mts.taskmanage.rest.presentation.model.GroupJoinAccept;
import jp.mts.taskmanage.rest.presentation.model.GroupJoinApply;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.junit.Test;

public class GroupJoinApiTest {

	@Tested GroupJoinApi target = new GroupJoinApi();
	@Injectable GroupJoinAppService groupJoinAppService;
	@Injectable GroupJoinSearchQuery groupSearchQuery;

	@Test
	public void test_applyJoin() {
		target.initialize();
		
		String groupId = "g01";
		String applicantMemberId = "m01";
		String applicationId = "a01";
		
		new Expectations() {{
			groupJoinAppService.applyJoin(groupId, applicantMemberId);
				result = new GroupJoinApplicationFixture(groupId,applicantMemberId).get();
		}};
		
		GroupJoinApply groupJoinApply = new GroupJoinApply();
		groupJoinApply.setGroupId(groupId);
		RestResponse<GroupJoinApply> response = target.apply(applicantMemberId, groupJoinApply);
		
		assertThat(response.getData().getJoinApplicationId(), is(applicationId));
	}

	@Test
	public void test_rejectGroupJoin() {
		target.initialize();
		
		new Expectations() {{
			groupJoinAppService.rejectJoin("g01", "a01");
				result = new GroupJoinApplicationFixture("g01", "a01").get();
		}};
		
		RestResponse<GroupJoinAccept> response = target.reject("g01", "a01");
		
		assertThat(response.getData().getJoinId(), is("a01"));
		
	}
}
