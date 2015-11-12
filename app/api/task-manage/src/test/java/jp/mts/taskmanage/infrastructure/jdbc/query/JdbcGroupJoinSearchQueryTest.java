package jp.mts.taskmanage.infrastructure.jdbc.query;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Timestamp;
import java.util.List;

import jp.mts.base.unittest.JdbcTestBase;
import jp.mts.libs.unittest.Dates;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery.AcceptableByAdminResult;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery.ByApplicantResult;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationStatus;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupJoinModel;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupMemberModel;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupModel;
import jp.mts.taskmanage.infrastructure.jdbc.model.MemberModel;

import org.junit.Before;
import org.junit.Test;

public class JdbcGroupJoinSearchQueryTest extends JdbcTestBase {
	
	JdbcGroupJoinSearchQuery target = new JdbcGroupJoinSearchQuery();

	@Before
	public void setupData() {
		MemberModel.deleteAll();
		GroupModel.deleteAll();
		GroupJoinModel.deleteAll();
		GroupMemberModel.deleteAll();
		
		new MemberModel().set(
				"member_id", "m01",
				"name", "member01")
			.saveIt();
		new MemberModel().set(
				"member_id", "m99",
				"name", "member99")
			.saveIt();
		new MemberModel().set(
				"member_id", "m98",
				"name", "member98")
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
		new GroupJoinModel().set(
				"application_id", "a02",
				"group_id", "g02",
				"applicant_id", "m99",
				"status", "APPLIED",
				"applied_time", Timestamp.valueOf("2015-11-01 12:00:00"))
			.saveIt();
		new GroupJoinModel().set(
				"application_id", "a03",
				"group_id", "g03",
				"applicant_id", "m98",
				"status", "APPLIED",
				"applied_time", Timestamp.valueOf("2015-11-01 13:00:00"))
			.saveIt();
		
		new GroupMemberModel().set(
				"group_id", "g01",
				"member_id", "m02",
				"admin", true)
			.saveIt();
		new GroupMemberModel().set(
				"group_id", "g03",
				"member_id", "m02",
				"admin", true)
			.saveIt();
	}

	@Test
	public void test_searchApplied() {
		List<ByApplicantResult> results = target.byApplicant("m99");
		
		assertThat(results.size(), is(2));
		assertThat(results.get(0).groupId, is("g01"));
		assertThat(results.get(0).groupName, is("グループA"));
		assertThat(results.get(0).ownerName, is("member01"));
		assertThat(results.get(0).joinApplicationStatus, is(GroupJoinApplicationStatus.APPLIED));
		assertThat(results.get(0).joinApplied, is(Dates.dateTime("2015/11/01 12:00:00.000")));
		assertThat(results.get(1).groupId, is("g02"));
		assertThat(results.get(1).groupName, is("groupB"));
		assertThat(results.get(1).ownerName, is("member01"));
		assertThat(results.get(1).joinApplicationStatus, is(GroupJoinApplicationStatus.APPLIED));
		assertThat(results.get(1).joinApplied, is(Dates.dateTime("2015/11/01 12:00:00.000")));
	}
	@Test
	public void test_searchAcceptable() {
		
		List<AcceptableByAdminResult> results = target.acceptableByAdmin("m02");
		
		assertThat(results.size(), is(2));
		assertThat(results.get(0).applicantId, is("m99"));
		assertThat(results.get(0).applicantName, is("member99"));
		assertThat(results.get(0).groupName, is("グループA"));
		assertThat(results.get(0).joinApplicationId, is("a01"));
		assertThat(results.get(0).joinApplied, is(Dates.dateTime("2015/11/01 12:00:00.000")));
		assertThat(results.get(1).applicantId, is("m98"));
		assertThat(results.get(1).applicantName, is("member98"));
		assertThat(results.get(1).groupName, is("GROUPA"));
		assertThat(results.get(1).joinApplicationId, is("a03"));
		assertThat(results.get(1).joinApplied, is(Dates.dateTime("2015/11/01 13:00:00.000")));
	}
}
