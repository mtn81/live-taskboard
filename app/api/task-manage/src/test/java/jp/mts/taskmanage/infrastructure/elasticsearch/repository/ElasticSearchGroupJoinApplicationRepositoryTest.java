package jp.mts.taskmanage.infrastructure.elasticsearch.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jp.mts.libs.unittest.Dates;
import jp.mts.taskmanage.domain.model.group.join.GroupJoin;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinFixture;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinId;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinStatus;
import jp.mts.taskmanage.infrastructure.elasticsearch.TaskManageESTestBase;

import org.junit.Before;
import org.junit.Test;

public class ElasticSearchGroupJoinApplicationRepositoryTest extends TaskManageESTestBase {
	
	ElasticSearchGroupJoinApplicationRepository target;
	
	@Before
	public void setup() {
		target = groupJoinApplicationRepository();
	}

	@Test public void 
	test_persistence() {
		target.save(new GroupJoinFixture("a01", "g01", "m01")
			.setStatus(GroupJoinStatus.ACCEPTED)
			.setApplied(Dates.dateShortTime("2016/01/02 12:00"))
			.get());
		
		GroupJoin found = target.findById(new GroupJoinId("a01")).get();
		
		assertThat(found.id().value(), is("a01"));
		assertThat(found.groupId().value(), is("g01"));
		assertThat(found.applicationMemberId().value(), is("m01"));
		assertThat(found.applied(), is(Dates.dateShortTime("2016/01/02 12:00")));
		assertThat(found.status(), is(GroupJoinStatus.ACCEPTED));
		
		assertThat(target.findById(new GroupJoinId("a02")).isPresent(), is(false));
	}

}
