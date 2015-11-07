package jp.mts.taskmanage.infrastructure.jdbc.query;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import jp.mts.base.unittest.JdbcTestBase;
import jp.mts.taskmanage.application.query.GroupSearchQuery.Result;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupModel;
import jp.mts.taskmanage.infrastructure.jdbc.model.MemberModel;

import org.junit.Test;

public class JdbcGroupSearchQueryTest extends JdbcTestBase {

	
	@Test
	public void test() {
	
		new MemberModel().set(
				"member_id", "m01",
				"name", "member01")
			.saveIt();
		new GroupModel().set(
				"group_id", "g01",
				"owner_member_id", "m01",
				"name", "グループA")
			.saveIt();
		new GroupModel().set(
				"group_id", "g02",
				"owner_member_id", "m01",
				"name", "groupB")
			.saveIt();
		new GroupModel().set(
				"group_id", "g03",
				"owner_member_id", "m01",
				"name", "GROUPA")
			.saveIt();
		new GroupModel().set(
				"group_id", "g04",
				"owner_member_id", "m01",
				"name", "My グループ B")
			.saveIt();
		new GroupModel().set(
				"group_id", "g05",
				"owner_member_id", "m02",
				"name", "グループA")
			.saveIt();
		
		List<Result> results = new JdbcGroupSearchQuery().byName("グループ");
		
		assertThat(results.size(), is(2));
		assertThat(results.get(0).getGroupId(), is("g01"));
		assertThat(results.get(0).getGroupName(), is("グループA"));
		assertThat(results.get(0).getOwnerName(), is("member01"));
		assertThat(results.get(1).getGroupId(), is("g04"));
		assertThat(results.get(1).getGroupName(), is("My グループ B"));
		assertThat(results.get(1).getOwnerName(), is("member01"));
		
	}

}
