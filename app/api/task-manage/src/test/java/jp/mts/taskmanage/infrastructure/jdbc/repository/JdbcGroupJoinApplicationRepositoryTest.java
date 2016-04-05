package jp.mts.taskmanage.infrastructure.jdbc.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.base.unittest.JdbcTestBase;
import jp.mts.libs.unittest.Dates;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplication;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationFixture;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationId;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationStatus;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JdbcGroupJoinApplicationRepositoryTest extends JdbcTestBase {

	@Test
	public void test_persistence() {
		
		JdbcGroupJoinApplicationRepository target = new JdbcGroupJoinApplicationRepository();

		target.save(
			new GroupJoinApplicationFixture("a01", "g01", "m01")
				.setStatus(GroupJoinApplicationStatus.ACCEPTED)
				.setApplied(Dates.dateTime("2015/11/01 12:00:00.000"))
				.get());

		GroupJoinApplication found = target.findById(
				new GroupJoinApplicationId("a01")).get();
		
		assertThat(found.id().value(), is("a01"));
		assertThat(found.groupId().value(), is("g01"));
		assertThat(found.applicationMemberId().value(), is("m01"));
		assertThat(found.status(), is(GroupJoinApplicationStatus.ACCEPTED));
		assertThat(found.applied(), is(Dates.dateTime("2015/11/01 12:00:00.000")));
	}

}
