package jp.mts.taskmanage.application;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import jp.mts.base.application.ApplicationException;
import jp.mts.base.domain.model.DomainCalendar;
import jp.mts.base.domain.model.DomainEventPublisher;
import jp.mts.base.domain.model.DomainObject;
import jp.mts.libs.unittest.Dates;
import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.GroupFixture;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.GroupJoinApplication;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationFixture;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationId;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationRepository;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationStatus;
import jp.mts.taskmanage.domain.model.GroupRepository;
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
	@Mocked DomainEventPublisher domainEventPublisher;
	
	@Before
	public void setup() {
		DomainObject.setDomainCalendar(domainCalendar);
	}
	
	@Test
	public void test_applyJoin() {
		DomainObject.setDomainEventPublisher(domainEventPublisher);
		
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
		DomainObject.setDomainEventPublisher(domainEventPublisher);
		
		GroupJoinApplication application = new GroupJoinApplicationFixture("a01", "g01", "m02").get();
		Group group = new GroupFixture("g01").get();
		new Expectations() {{
			groupJoinRepository.findById(new GroupJoinApplicationId("a01"));
				result = Optional.of(application);
			groupRepository.findById(new GroupId("g01"));
				result = Optional.of(group);
			
			groupJoinRepository.save(application);
		}};
		
		GroupJoinApplication actual = target.rejectJoin("g01", "a01");

		assertThat(actual.id().value(), is("a01"));
		assertThat(actual.status(), is(GroupJoinApplicationStatus.REJECTED));
	}
	@Test
	public void test_rejectJoin__group_is_mismatch() {
		
		GroupJoinApplication application = new GroupJoinApplicationFixture("a01", "g01", "m02").get();
		Group group = new GroupFixture("g02").get();
		new Expectations() {{
			groupJoinRepository.findById(new GroupJoinApplicationId("a01"));
				result = Optional.of(application);
			groupRepository.findById(new GroupId("g02"));
				result = Optional.of(group);
		}};
		
		try {
			target.rejectJoin("g02", "a01");
		} catch (ApplicationException e) {
			assertThat(e.hasErrorOf(ErrorType.CANNOT_REJECT_JOIN), is(true));
		} 
	}

}
