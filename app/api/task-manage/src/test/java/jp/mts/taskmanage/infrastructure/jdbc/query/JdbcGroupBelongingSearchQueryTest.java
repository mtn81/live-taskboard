package jp.mts.taskmanage.infrastructure.jdbc.query;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import jp.mts.base.unittest.JdbcTestBase;
import jp.mts.taskmanage.application.query.GroupBelongingSearchQuery.GroupSummary;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupMemberModel;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupModel;
import jp.mts.taskmanage.infrastructure.jdbc.model.MemberModel;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JdbcGroupBelongingSearchQueryTest extends JdbcTestBase {

	private JdbcGroupBelongingSearchQuery target = new JdbcGroupBelongingSearchQuery();
	
	@Test
	public void test_byMember() {

		MemberModel.createIt(
				"member_id", "m01",
				"name", "member01",
				"type", "PROPER");
		MemberModel.createIt(
				"member_id", "m02",
				"name", "member02",
				"type", "PROPER");
		
		GroupModel.createIt(
			"group_id", "g01",
			"owner_member_id", "m99",
			"name", "group01",
			"description", "");
		GroupModel.createIt(
			"group_id", "g02",
			"owner_member_id", "m99",
			"name", "group02",
			"description", "");
		GroupModel.createIt(
			"group_id", "g03",
			"owner_member_id", "m99",
			"name", "group03",
			"description", "");

		GroupMemberModel.createIt(
			"group_id", "g01",
			"member_id", "m01",
			"admin", true);
		GroupMemberModel.createIt(
			"group_id", "g02",
			"member_id", "m01",
			"admin", false);
		GroupMemberModel.createIt(
			"group_id", "g03",
			"member_id", "m02",
			"admin", true);

		List<GroupSummary> results = target.byMember("m01");
		
		assertThat(results.size(), is(2));
		assertThat(results.get(0).groupId, is("g01"));
		assertThat(results.get(0).groupName, is("group01"));
		assertThat(results.get(0).isAdmin, is(true));
		assertThat(results.get(1).groupId, is("g02"));
		assertThat(results.get(1).groupName, is("group02"));
		assertThat(results.get(1).isAdmin, is(false));
	}

}
