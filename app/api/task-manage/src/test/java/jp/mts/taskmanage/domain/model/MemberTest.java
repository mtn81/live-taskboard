package jp.mts.taskmanage.domain.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import jp.mts.base.domain.model.DomainEventPublisher;
import jp.mts.base.domain.model.DomainObject;
import mockit.Mocked;

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

}
