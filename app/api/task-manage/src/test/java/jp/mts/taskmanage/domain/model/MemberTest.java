package jp.mts.taskmanage.domain.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class MemberTest {

	@Test
	public void test_createGroup() {
		Member member = new MemberFixture().get();
		GroupId groupId = new GroupId("g01");
		Group newGroup = member.createGroup(groupId, "group name", "group creation");
		
		assertThat(newGroup.groupId(), is(groupId));
		assertThat(newGroup.ownerMemberId(), is(member.memberId()));
		assertThat(newGroup.name(), is("group name"));
		assertThat(newGroup.description(), is("group creation"));

	}

}
