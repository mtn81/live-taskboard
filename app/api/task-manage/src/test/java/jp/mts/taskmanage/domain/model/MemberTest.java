package jp.mts.taskmanage.domain.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventPublisher;
import jp.mts.base.domain.model.DomainObject;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.internal.expectations.argumentMatching.CaptureMatcher;

import org.junit.Test;

public class MemberTest {
	
	@Mocked DomainEventPublisher domainEventPublisher;

	@Test public void 
	test_createGroup() {
		DomainObject.setDomainEventPublisher(domainEventPublisher);
		Member member = new MemberFixture().get();
		GroupId groupId = new GroupId("g01");
		
		Group newGroup = member.createGroup(groupId, "group name", "group creation");
		
		assertThat(newGroup.groupId(), is(groupId));
		assertThat(newGroup.ownerMemberId(), is(member.memberId()));
		assertThat(newGroup.name(), is("group name"));
		assertThat(newGroup.description(), is("group creation"));
	}
	
	@Test public void
	test_changeAttributes() {
		
		Member member = new MemberFixture().get();
		member.changeAttributes("new name", "new email");
		
		assertThat(member.name(), is("new name"));
		assertThat(member.email(), is("new email"));
	}
	
	@Test public void
	test_applyJoin() {
		//setup
		DomainObject.setDomainEventPublisher(domainEventPublisher);

		GroupJoinApplicationId applicationId = new GroupJoinApplicationId("a01");
		Group group =  new GroupFixture().get();
		Member member = new MemberFixture().get();
		
		//execute
		GroupJoinApplication actual = member.applyJoinTo(applicationId, group);
		
		//verify
		assertThat(actual.applicationMemberId(), is(member.memberId()));
		assertThat(actual.groupId(), is(group.id()));
		assertThat(actual.id(), is(applicationId));

		new Verifications() {{
			GroupJoinApplicated event;
			domainEventPublisher.publish(event = withCapture());
			
			assertThat(event.getGroupId(), is("g01"));
			assertThat(event.getApplicantId(), is("m01"));
		}};
	}

}
