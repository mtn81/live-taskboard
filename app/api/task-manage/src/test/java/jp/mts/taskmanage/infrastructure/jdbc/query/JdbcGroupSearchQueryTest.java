package jp.mts.taskmanage.infrastructure.jdbc.query;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Timestamp;
import java.util.List;

import jp.mts.base.unittest.JdbcTestBase;
import jp.mts.libs.unittest.Dates;
import jp.mts.taskmanage.application.query.GroupSearchQuery.Result;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationStatus;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupJoinModel;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupModel;
import jp.mts.taskmanage.infrastructure.jdbc.model.MemberModel;

import org.junit.Before;
import org.junit.Test;

public class JdbcGroupSearchQueryTest extends JdbcTestBase {

	@Before
	public void setupData() {
		MemberModel.deleteAll();
		GroupModel.deleteAll();
		GroupJoinModel.deleteAll();
		
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

		new GroupJoinModel().set(
				"application_id", "a01",
				"group_id", "g01",
				"applicant_id", "m99",
				"status", "APPLIED",
				"applied_time", Timestamp.valueOf("2015-11-01 12:00:00"))
			.saveIt();
	}
	
	@Test
	public void test_searchNotApplied() {
	
		List<Result> results = new JdbcGroupSearchQuery().notJoinAppliedByName("m99", "グループ");
		
		assertThat(results.size(), is(1));
		assertThat(results.get(0).getGroupId(), is("g04"));
		assertThat(results.get(0).getGroupName(), is("My グループ B"));
		assertThat(results.get(0).getOwnerName(), is("member01"));
		assertThat(results.get(0).getJoinApplicationStatus(), is(nullValue()));
		assertThat(results.get(0).getJoinApplied(), is(nullValue()));
	}

	@Test
	public void test_searchApplied() {
		List<Result> results = new JdbcGroupSearchQuery().joinApplied("m99");
		
		assertThat(results.size(), is(1));
		assertThat(results.get(0).getGroupId(), is("g01"));
		assertThat(results.get(0).getGroupName(), is("グループA"));
		assertThat(results.get(0).getOwnerName(), is("member01"));
		assertThat(results.get(0).getJoinApplicationStatus(), is(GroupJoinApplicationStatus.APPLIED));
		assertThat(results.get(0).getJoinApplied(), is(Dates.dateTime("2015/11/01 12:00:00.000")));
	}
}
