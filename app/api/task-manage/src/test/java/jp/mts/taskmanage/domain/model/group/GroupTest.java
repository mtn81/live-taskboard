package jp.mts.taskmanage.domain.model.group;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jp.mts.base.domain.model.DomainEventPublisher;
import jp.mts.base.domain.model.DomainObject;
import jp.mts.taskmanage.domain.model.group.Group;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinAccepted;
import jp.mts.taskmanage.domain.model.group.join.GroupJoin;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinFixture;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinRejected;
import mockit.Mocked;
import mockit.Verifications;

import org.junit.Test;

public class GroupTest {
	@Mocked DomainEventPublisher domainEventPublisher;

	@Test public void 
	test_acceptJoin__accept_same_group_join() {
		//setup
		DomainObject.setDomainEventPublisher(domainEventPublisher);
		Group target = new GroupFixture("g01").get();
		GroupJoin application = new GroupJoinFixture("g01", "m01").get();
		
		//execute
		boolean actual = target.accept(application);
		
		//verify
		assertThat(actual, is(true));
		
		new Verifications() {{
			GroupJoinAccepted event;
			domainEventPublisher.publish(event = withCapture());
				times = 1;
			
			assertThat(event.getGroupId(), is("g01"));
			assertThat(event.getMemberId(), is("m01"));
		}};
	}
	@Test public void 
	test_acceptJoin__fail_for_another_group_join() {
		Group target = new GroupFixture("g02").get();
		GroupJoin application = new GroupJoinFixture("g01", "m01").get();
		
		boolean actual = target.accept(application);
		
		assertThat(actual, is(false));
	}

	@Test public void 
	test_rejectJoin__success_for_same_group_join() {
		DomainObject.setDomainEventPublisher(domainEventPublisher);
		Group target = new GroupFixture("g01").get();
		GroupJoin application = new GroupJoinFixture("g01", "m01").get();
		
		boolean actual = target.reject(application);

		assertThat(actual, is(true));
		
		new Verifications() {{
			GroupJoinRejected event;
			domainEventPublisher.publish(event = withCapture());
				times = 1;
			
			assertThat(event.getGroupId(), is("g01"));
			assertThat(event.getMemberId(), is("m01"));
		}};
	}
	@Test public void 
	test_rejectJoin__fail_for_another_group_join() {
		Group target = new GroupFixture("g02").get();
		GroupJoin application = new GroupJoinFixture("g01", "m01").get();
		
		boolean actual = target.reject(application);

		assertThat(actual, is(false));
	}

}
