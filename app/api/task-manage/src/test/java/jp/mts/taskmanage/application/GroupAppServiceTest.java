package jp.mts.taskmanage.application;

import static jp.mts.taskmanage.application.ErrorType.GROUP_REMOVE_DISABLED;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
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
import jp.mts.taskmanage.domain.model.group.join.GroupJoinRepository;
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
	@Injectable GroupJoinRepository groupJoinRepository;
	@Mocked DomainEventPublisher domainEventPublisher;
	@Mocked DomainCalendar domainCalendar;
	
	@Before
	public void setup() {
		DomainObject.setDomainEventPublisher(domainEventPublisher);
		DomainObject.setDomainCalendar(domainCalendar);
	}

	@Test public void 
	test_group_creation() {
		Member member = new MemberFixture().get();
		GroupId groupId = new GroupId("g01");
		Group registered = new GroupFixture().get();

		new Expectations(member) {{
			memberRepository.findById(new MemberId("m01"));
				result = Optional.of(member);
			groupRepository.newGroupId();
				result = groupId;
			
			member.createGroup(groupId, "group01", "this is a test group");
				result = registered;
				
			groupRepository.save(registered);
		}};

		Group group = target.registerGroup("m01", "group01", "this is a test group");
		
		assertThat(group, is(sameInstance(registered)));
	}
	
	@Test public void 
	test_removeGroup__group_removed() {
		
		String memberId = "m01";
		String groupId = "g01";
		
		Member member = new MemberFixture().get();
		Group removeTarget = new GroupFixture().get();
		
		new Expectations(member) {{
			memberRepository.findById(new MemberId(memberId));
				result = Optional.of(member);
			groupRepository.findById(new GroupId(groupId));
				result = Optional.of(removeTarget);
				
			member.remove(removeTarget);
				result = true;
				
			groupRepository.remove(removeTarget);
		}};

		target.removeGroup(memberId, groupId);
	}
	@Test public void 
	test_removeGroup__group_not_removed() {
		
		String memberId = "m01";
		String groupId = "g01";
		
		Member member = new MemberFixture().get();
		Group removeTarget = new GroupFixture().get();
		
		new Expectations(member) {{
			memberRepository.findById(new MemberId(memberId));
				result = Optional.of(member);

			groupRepository.findById(new GroupId(groupId));
				result = Optional.of(removeTarget);
				
			member.remove(removeTarget);
				result = false;
		}};
		
		try{
			target.removeGroup(memberId, groupId);
		}catch(ApplicationException e){
			assertThat(e.hasErrorOf(GROUP_REMOVE_DISABLED), is(true));
		}
	}

}
