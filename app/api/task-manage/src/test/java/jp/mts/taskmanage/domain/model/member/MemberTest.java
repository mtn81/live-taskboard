package jp.mts.taskmanage.domain.model.member;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.base.domain.model.DomainEventPublisher;
import jp.mts.base.domain.model.DomainObject;
import jp.mts.taskmanage.domain.model.group.Group;
import jp.mts.taskmanage.domain.model.group.GroupFixture;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicated;
import jp.mts.taskmanage.domain.model.group.join.GroupJoin;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinId;
import mockit.Mocked;
import mockit.Verifications;

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

		GroupJoinId applicationId = new GroupJoinId("a01");
		Group group =  new GroupFixture().get();
		Member member = new MemberFixture().get();
		
		//execute
		GroupJoin actual = member.applyJoinTo(applicationId, group);
		
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

	@Test public void
	test_group_can_be_removed_by_admin() {
		//setup
		DomainObject.setDomainEventPublisher(domainEventPublisher);

		Group group =  new GroupFixture("g01").get();
		Member member = new MemberFixture().addGroupBelonging("g01", true).get();
		
		boolean actual = member.remove(group);
		
		assertThat(actual, is(true));
	}
	
	@Test public void
	test_group_cannot_be_removed_by_normal() {
		//setup
		DomainObject.setDomainEventPublisher(domainEventPublisher);

		Group group =  new GroupFixture("g01").get();
		Member member = new MemberFixture().addGroupBelonging("g01", false).get();
		
		boolean actual = member.remove(group);
		
		assertThat(actual, is(false));
	}
	
	
}
