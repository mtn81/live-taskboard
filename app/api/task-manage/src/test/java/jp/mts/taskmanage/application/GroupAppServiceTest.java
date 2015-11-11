package jp.mts.taskmanage.application;

import static com.google.common.collect.Lists.newArrayList;
import static jp.mts.taskmanage.application.ErrorType.GROUP_REMOVE_DISABLED;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import jp.mts.base.application.ApplicationException;
import jp.mts.base.domain.model.DomainCalendar;
import jp.mts.base.domain.model.DomainEventPublisher;
import jp.mts.base.domain.model.DomainObject;
import jp.mts.libs.unittest.Dates;
import jp.mts.taskmanage.application.GroupAppService.GroupBelongingPair;
import jp.mts.taskmanage.application.query.GroupSearchQuery;
import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.GroupBelonging;
import jp.mts.taskmanage.domain.model.GroupBelongingFixture;
import jp.mts.taskmanage.domain.model.GroupBelongingRepository;
import jp.mts.taskmanage.domain.model.GroupFixture;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.GroupJoinApplication;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationFixture;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationId;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationRepository;
import jp.mts.taskmanage.domain.model.GroupRepository;
import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.MemberFixture;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.domain.model.MemberRepository;
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
	@Injectable GroupBelongingRepository groupBelongingRepository;
	@Injectable GroupSearchQuery groupSearchQuery;
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
				result = new MemberFixture().get();
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
		
		Group removeTarget = new GroupFixture(groupId, memberId).get();
		GroupBelonging adminGroupBelonging = new GroupBelongingFixture().setAdmin(true).get();
		
		new Expectations() {{
			groupBelongingRepository.findById(new MemberId(memberId), new GroupId(groupId));
				result = adminGroupBelonging;
			groupRepository.findById(new GroupId(groupId));
				result = removeTarget;
			groupRepository.remove(removeTarget);
		}};

		target.removeGroup(memberId, groupId);
	}
	@Test
	public void test_group_cant_be_removed_with_normal_user() {
		
		String memberId = "m01";
		String groupId = "g01";
		
		GroupBelonging adminGroupBelonging = new GroupBelongingFixture().setAdmin(false).get();
		
		new Expectations() {{
			groupBelongingRepository.findById(new MemberId(memberId), new GroupId(groupId));
				result = adminGroupBelonging;
		}};
		
		try{
			target.removeGroup(memberId, groupId);
		}catch(ApplicationException e){
			assertThat(e.hasErrorOf(GROUP_REMOVE_DISABLED), is(true));
		}
	}
	
	@Test
	public void test_member_absent_error_on_group_creation() {
		new Expectations() {{
			memberRepository.findById(new MemberId("m01"));
				result = null;
		}};

		try{
			target.registerGroup("m01", "group01", "this is a test group");
		}catch(ApplicationException e){
			assertThat(e.hasErrorOf(ErrorType.MEMBER_NOT_EXIST), is(true));
		}
	}

	@Test
	public void test_list_belonging_group() {

		new Expectations() {{
			groupBelongingRepository.findByMember(new MemberId("m01"));
				result = newArrayList(
						new GroupBelongingFixture("g01", "m01").setAdmin(true).get(),
						new GroupBelongingFixture("g02", "m01").setAdmin(false).get());

			groupRepository.findByIds(newArrayList(new GroupId("g01"), new GroupId("g02")));
				result = newArrayList(
						new GroupFixture("g01").get(), 
						new GroupFixture("g02").get());
		}};
		List<GroupBelongingPair> groups = target.listGroupBelongingFor("m01");
		
		assertThat(groups.size(), is(2));
		assertThat(groups.get(0).getGroup().groupId().value(), is("g01"));
		assertThat(groups.get(1).getGroup().groupId().value(), is("g02"));
	}
	
	@Test
	public void test_member_entry() {
		
		String groupId = "g01";
		Group group = new GroupFixture(groupId).get();
		String memberId = "m01";
		Member member = new MemberFixture(memberId).get();
		GroupBelonging groupBelonging = new GroupBelongingFixture(groupId, memberId).get();
		
		new Expectations() {{
			groupRepository.findById(new GroupId(groupId));
				result = group;
			memberRepository.findById(new MemberId(memberId));
				result = member;
			groupBelongingRepository.save(groupBelonging);
		}};
		
		target.entryGroupAsAdministrator(groupId, memberId);
	}
	
	@Test
	public void test_applyJoin() {
		
		GroupJoinApplication groupJoinApplication = new GroupJoinApplicationFixture("a01", "g01", "m01").get();
		new Expectations() {{
			domainCalendar.systemDate();
				result = Dates.dateTime("2015/11/01 12:00:00.000");
			
			memberRepository.findById(new MemberId("m01"));
				result = new MemberFixture("m01").get();
			groupRepository.findById(new GroupId("g01"));
				result = new GroupFixture("g01").get();
			
			groupJoinRepository.newId();
				result = new GroupJoinApplicationId("a01");
			groupJoinRepository.save(groupJoinApplication);
		}};
		
		GroupJoinApplication actual = target.applyJoin("g01", "m01");
		
		assertThat(actual.id().value(), is("a01"));
		assertThat(actual.groupId().value(), is("g01"));
		assertThat(actual.applicationMemberId().value(), is("m01"));
		assertThat(actual.applied(), is(Dates.dateTime("2015/11/01 12:00:00.000")));
	}
	
}
