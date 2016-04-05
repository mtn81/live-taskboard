package jp.mts.taskmanage.application;

import static jp.mts.taskmanage.application.ErrorType.GROUP_REMOVE_DISABLED;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import jp.mts.base.application.ApplicationException;
import jp.mts.base.domain.model.DomainCalendar;
import jp.mts.base.domain.model.DomainEventPublisher;
import jp.mts.base.domain.model.DomainObject;
import jp.mts.taskmanage.domain.model.group.Group;
import jp.mts.taskmanage.domain.model.group.GroupFixture;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.group.GroupRepository;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationRepository;
import jp.mts.taskmanage.domain.model.member.Member;
import jp.mts.taskmanage.domain.model.member.MemberFixture;
import jp.mts.taskmanage.domain.model.member.MemberId;
import jp.mts.taskmanage.domain.model.member.MemberRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;

import org.junit.Before;
import org.junit.Test;

public class GroupAppServiceTest {

	@Tested GroupAppService target = new GroupAppService();
	@Injectable GroupRepository groupRepository;
	@Injectable MemberRepository memberRepository;
	@Injectable GroupJoinApplicationRepository groupJoinRepository;
	@Mocked DomainEventPublisher domainEventPublisher;
	@Mocked DomainCalendar domainCalendar;
	
	@Before
	public void setup() {
		DomainObject.setDomainEventPublisher(domainEventPublisher);
		DomainObject.setDomainCalendar(domainCalendar);
	}

	@Test
	public void test_group_creation() {
		new Expectations() {{
			memberRepository.findById(new MemberId("m01"));
				result = Optional.of(new MemberFixture().get());
			groupRepository.newGroupId();
				result = new GroupId("g01");
				
			groupRepository.save((Group)any);
		}};

		Group group = target.registerGroup("m01", "group01", "this is a test group");
		
		assertThat(group.groupId().value(), is("g01"));
		assertThat(group.ownerMemberId().value(), is("m01"));
		assertThat(group.name(), is("group01"));
		assertThat(group.description(), is("this is a test group"));
	}
	
	@Test
	public void test_group_can_removed_with_admin_user() {
		
		String memberId = "m01";
		String groupId = "g01";
		
		Member member = new MemberFixture("m01")
			.addGroupBelonging("g01", true)
			.get();
		Group removeTarget = new GroupFixture(groupId, memberId).get();
		
		new Expectations() {{
			memberRepository.findById(new MemberId(memberId));
				result = Optional.of(member);
			groupRepository.findById(new GroupId(groupId));
				result = Optional.of(removeTarget);
			groupRepository.remove(removeTarget);
		}};

		target.removeGroup(memberId, groupId);
	}
	@Test
	public void test_group_cant_be_removed_with_normal_user() {
		
		String memberId = "m01";
		String groupId = "g01";
		
		Member member = new MemberFixture("m01")
			.addGroupBelonging("g01", false)
			.get();
		Group removeTarget = new GroupFixture(groupId, memberId).get();
		
		new Expectations() {{
			memberRepository.findById(new MemberId(memberId));
				result = Optional.of(member);

			groupRepository.findById(new GroupId(groupId));
				result = Optional.of(removeTarget);
		}};
		
		try{
			target.removeGroup(memberId, groupId);
		}catch(ApplicationException e){
			assertThat(e.hasErrorOf(GROUP_REMOVE_DISABLED), is(true));
		}
	}

}
