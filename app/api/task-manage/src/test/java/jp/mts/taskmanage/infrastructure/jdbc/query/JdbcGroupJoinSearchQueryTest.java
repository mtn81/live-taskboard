package jp.mts.taskmanage.infrastructure.jdbc.query;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Timestamp;
import java.util.List;

import jp.mts.base.unittest.JdbcTestBase;
import jp.mts.libs.unittest.Dates;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery.ByAdminResult;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery.ByApplicantResult;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery.NotJoinAppliedWithNameResult;
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
	}

	@Test
	public void test_searchApplied() {
		
		new MemberModel().set(
				"member_id", "m01",
				"name", "member01",
				"type", "PROPER")
			.saveIt();
		new MemberModel().set(
				"member_id", "m99",
				"name", "member99",
				"type", "PROPER")
			.saveIt();

		new GroupModel().set(
				"group_id", "g01",
				"owner_member_id", "m01",
				"name", "グループ1")
			.saveIt();
		new GroupModel().set(
				"group_id", "g02",
				"owner_member_id", "m01",
				"name", "グループ2")
			.saveIt();
		new GroupModel().set(
				"group_id", "g03",
				"owner_member_id", "m01",
				"name", "グループ3")
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
				"applicant_id", "m99",
				"status", "REJECTED",
				"applied_time", Timestamp.valueOf("2015-11-01 12:00:00"))
			.saveIt();
		new GroupJoinModel().set(
				"application_id", "a04",
				"group_id", "g02",
				"applicant_id", "m01",
				"status", "APPLIED",
				"applied_time", Timestamp.valueOf("2015-11-01 12:00:00"))
			.saveIt();
		
		List<ByApplicantResult> results = target.byApplicant("m99");
		
		assertThat(results.size(), is(3));
		assertThat(results.get(0).groupId, is("g01"));
		assertThat(results.get(0).groupName, is("グループ1"));
		assertThat(results.get(0).ownerName, is("member01"));
		assertThat(results.get(0).joinApplicationStatus, is(GroupJoinApplicationStatus.APPLIED));
		assertThat(results.get(0).joinApplied, is(Dates.dateTime("2015/11/01 12:00:00.000")));

		assertThat(results.get(1).groupId, is("g02"));
		assertThat(results.get(1).groupName, is("グループ2"));
		assertThat(results.get(1).ownerName, is("member01"));
		assertThat(results.get(1).joinApplicationStatus, is(GroupJoinApplicationStatus.APPLIED));
		assertThat(results.get(1).joinApplied, is(Dates.dateTime("2015/11/01 12:00:00.000")));

		assertThat(results.get(2).groupId, is("g03"));
		assertThat(results.get(2).groupName, is("グループ3"));
		assertThat(results.get(2).ownerName, is("member01"));
		assertThat(results.get(2).joinApplicationStatus, is(GroupJoinApplicationStatus.REJECTED));
		assertThat(results.get(2).joinApplied, is(Dates.dateTime("2015/11/01 12:00:00.000")));
	}
	@Test
	public void test_searchAcceptable() {
		
		new MemberModel().set(
				"member_id", "m01",
				"name", "member01",
				"type", "PROPER")
			.saveIt();
		new MemberModel().set(
				"member_id", "m02",
				"name", "member01",
				"type", "PROPER")
			.saveIt();
		new MemberModel().set(
				"member_id", "m99",
				"name", "member99",
				"type", "PROPER")
			.saveIt();

		new GroupModel().set(
				"group_id", "g01",
				"owner_member_id", "m01",
				"name", "グループ1")
			.saveIt();
		new GroupModel().set(
				"group_id", "g02",
				"owner_member_id", "m02",
				"name", "グループ2")
			.saveIt();
		new GroupModel().set(
				"group_id", "g03",
				"owner_member_id", "m02",
				"name", "グループ3")
			.saveIt();
		new GroupModel().set(
				"group_id", "g04",
				"owner_member_id", "m02",
				"name", "グループ4")
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
				"applicant_id", "m99",
				"status", "APPLIED",
				"applied_time", Timestamp.valueOf("2015-11-01 12:00:00"))
			.saveIt();
		new GroupJoinModel().set(
				"application_id", "a04",
				"group_id", "g04",
				"applicant_id", "m99",
				"status", "REJECTED",
				"applied_time", Timestamp.valueOf("2015-11-01 12:00:00"))
			.saveIt();
		
		new GroupMemberModel().set(
				"group_id", "g01",
				"member_id", "m01",
				"admin", true)
			.saveIt();
		new GroupMemberModel().set(
				"group_id", "g02",
				"member_id", "m01",
				"admin", true)
			.saveIt();
		new GroupMemberModel().set(
				"group_id", "g04",
				"member_id", "m01",
				"admin", true)
			.saveIt();
		
		List<ByAdminResult> results = target.acceptableByAdmin("m01");
		
		assertThat(results.size(), is(2));
		assertThat(results.get(0).applicantId, is("m99"));
		assertThat(results.get(0).applicantName, is("member99"));
		assertThat(results.get(0).groupName, is("グループ1"));
		assertThat(results.get(0).joinApplicationId, is("a01"));
		assertThat(results.get(0).joinApplied, is(Dates.dateTime("2015/11/01 12:00:00.000")));

		assertThat(results.get(1).applicantId, is("m99"));
		assertThat(results.get(1).applicantName, is("member99"));
		assertThat(results.get(1).groupName, is("グループ2"));
		assertThat(results.get(1).joinApplicationId, is("a02"));
		assertThat(results.get(1).joinApplied, is(Dates.dateTime("2015/11/01 12:00:00.000")));
	}
	@Test
	public void test_searchRejected() {
		new MemberModel().set(
				"member_id", "m01",
				"name", "member01",
				"type", "PROPER")
			.saveIt();
		new MemberModel().set(
				"member_id", "m02",
				"name", "member01",
				"type", "PROPER")
			.saveIt();
		new MemberModel().set(
				"member_id", "m99",
				"name", "member99",
				"type", "PROPER")
			.saveIt();

		new GroupModel().set(
				"group_id", "g01",
				"owner_member_id", "m01",
				"name", "グループ1")
			.saveIt();
		new GroupModel().set(
				"group_id", "g02",
				"owner_member_id", "m02",
				"name", "グループ2")
			.saveIt();
		new GroupModel().set(
				"group_id", "g03",
				"owner_member_id", "m02",
				"name", "グループ3")
			.saveIt();
		new GroupModel().set(
				"group_id", "g04",
				"owner_member_id", "m02",
				"name", "グループ4")
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
				"status", "REJECTED",
				"applied_time", Timestamp.valueOf("2015-11-01 12:00:00"))
			.saveIt();
		new GroupJoinModel().set(
				"application_id", "a03",
				"group_id", "g03",
				"applicant_id", "m99",
				"status", "APPLIED",
				"applied_time", Timestamp.valueOf("2015-11-01 12:00:00"))
			.saveIt();
		new GroupJoinModel().set(
				"application_id", "a04",
				"group_id", "g04",
				"applicant_id", "m99",
				"status", "REJECTED",
				"applied_time", Timestamp.valueOf("2015-11-01 12:00:00"))
			.saveIt();
		
		new GroupMemberModel().set(
				"group_id", "g01",
				"member_id", "m01",
				"admin", true)
			.saveIt();
		new GroupMemberModel().set(
				"group_id", "g02",
				"member_id", "m01",
				"admin", true)
			.saveIt();
		
		List<ByAdminResult> results = target.rejectedByAdmin("m01");
		
		assertThat(results.size(), is(1));
		assertThat(results.get(0).applicantId, is("m99"));
		assertThat(results.get(0).applicantName, is("member99"));
		assertThat(results.get(0).groupName, is("グループ2"));
		assertThat(results.get(0).joinApplicationId, is("a02"));
		assertThat(results.get(0).joinApplied, is(Dates.dateTime("2015/11/01 12:00:00.000")));
	}
	
	@Test
	public void test_searchNotApplied() {
	
		new MemberModel().set(
				"member_id", "m01",
				"name", "member01",
				"type", "PROPER")
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

		List<NotJoinAppliedWithNameResult> results = target.notJoinAppliedWithName("m99", "グループ");
		
		assertThat(results.size(), is(1));
		assertThat(results.get(0).getGroupId(), is("g04"));
		assertThat(results.get(0).getGroupName(), is("My グループ B"));
		assertThat(results.get(0).getOwnerName(), is("member01"));
	}
	
}
