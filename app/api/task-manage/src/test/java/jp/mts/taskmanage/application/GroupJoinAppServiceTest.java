package jp.mts.taskmanage.application;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import jp.mts.base.application.ApplicationException;
import jp.mts.base.domain.model.DomainCalendar;
import jp.mts.base.domain.model.DomainObject;
import jp.mts.libs.unittest.Dates;
import jp.mts.taskmanage.domain.model.GroupFixture;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.GroupJoinApplication;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationFixture;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationId;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationRepository;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationStatus;
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

public class GroupJoinAppServiceTest {

	
	@Tested GroupJoinAppService target = new GroupJoinAppService();
	@Injectable GroupRepository groupRepository;
	@Injectable MemberRepository memberRepository;
	@Injectable GroupJoinApplicationRepository groupJoinRepository;
	@Mocked DomainCalendar domainCalendar;
	
	@Before
	public void setup() {
		DomainObject.setDomainCalendar(domainCalendar);
	}
	
	@Test
	public void test_applyJoin() {
		
		GroupJoinApplication groupJoinApplication = new GroupJoinApplicationFixture("a01", "g01", "m01").get();
		new Expectations() {{
			domainCalendar.systemDate();
				result = Dates.dateTime("2015/11/01 12:00:00.000");
			
			memberRepository.findById(new MemberId("m01"));
				result = Optional.of(new MemberFixture("m01").get());
			groupRepository.findById(new GroupId("g01"));
				result = Optional.of(new GroupFixture("g01").get());
			
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
	
	@Test
	public void test_rejectJoin() {
		
		GroupJoinApplication application = new GroupJoinApplicationFixture("a01", "g01", "m02").get();
		Member member = new MemberFixture("m01").addGroupBelonging("g01", true).get();
		new Expectations() {{
			groupJoinRepository.findById(new GroupJoinApplicationId("a01"));
				result = Optional.of(application);
			memberRepository.findById(new MemberId("m01"));
				result = Optional.of(member);
			
			groupJoinRepository.save(application);
		}};
		
		GroupJoinApplication actual = target.rejectJoin("a01", "m01");

		assertThat(actual.id().value(), is("a01"));
		assertThat(actual.status(), is(GroupJoinApplicationStatus.REJECTED));
	}
	@Test
	public void test_rejectJoin__member_is_not_group_admin() {
		
		GroupJoinApplication application = new GroupJoinApplicationFixture("a01", "g01", "m02").get();
		Member member = new MemberFixture("m01").addGroupBelonging("g01", false).get();
		new Expectations() {{
			groupJoinRepository.findById(new GroupJoinApplicationId("a01"));
				result = Optional.of(application);
			memberRepository.findById(new MemberId("m01"));
				result = Optional.of(member);
		}};
		
		try {
			target.rejectJoin("a01", "m01");
		} catch (ApplicationException e) {
			assertThat(e.hasErrorOf(ErrorType.CANNOT_ACCEPT_JOIN), is(true));
		} 
	}

}
