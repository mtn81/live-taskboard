package jp.mts.taskmanage.application;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import jp.mts.base.application.ApplicationException;
import jp.mts.taskmanage.domain.model.group.Group;
import jp.mts.taskmanage.domain.model.group.GroupFixture;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.group.GroupRepository;
import jp.mts.taskmanage.domain.model.group.join.GroupJoin;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinFixture;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinId;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinRepository;
import jp.mts.taskmanage.domain.model.member.Member;
import jp.mts.taskmanage.domain.model.member.MemberFixture;
import jp.mts.taskmanage.domain.model.member.MemberId;
import jp.mts.taskmanage.domain.model.member.MemberRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.junit.Test;


public class GroupJoinAppServiceTest {

	@Tested GroupJoinAppService target = new GroupJoinAppService();
	@Injectable GroupRepository groupRepository;
	@Injectable MemberRepository memberRepository;
	@Injectable GroupJoinRepository groupJoinRepository;
	
	@Test public void 
	test_applyJoin() {
		Group group = new GroupFixture().get();
		Member member = new MemberFixture().get();
		GroupJoinId applicationId = new GroupJoinId("a01");
		GroupJoin groupJoinApplication = new GroupJoinFixture().get();

		new Expectations(member) {{
			memberRepository.findById(new MemberId("m01"));
				result = Optional.of(member);
			groupRepository.findById(new GroupId("g01"));
				result = Optional.of(group);
			
			groupJoinRepository.newId();
				result = applicationId;
				
			member.applyJoinTo(applicationId, group);
				result = groupJoinApplication;
				
			groupJoinRepository.save(groupJoinApplication);
		}};
		
		GroupJoin actual = target.applyJoin("g01", "m01");
		
		assertThat(actual, is(sameInstance(groupJoinApplication)));
	}
	
	@Test public void 
	test_rejectJoin__join_rejected() {

		GroupJoin application = new GroupJoinFixture().get();
		Group group = new GroupFixture().get();
		new Expectations(group) {{
			groupJoinRepository.findById(new GroupJoinId("a01"));
				result = Optional.of(application);
			groupRepository.findById(new GroupId("g01"));
				result = Optional.of(group);
				
			group.reject(application);
				result = true;
			
			groupJoinRepository.save(application);
		}};
		
		GroupJoin actual = target.rejectJoin("g01", "a01");

		assertThat(actual, is(sameInstance(application)));
	}
	@Test public void 
	test_rejectJoin__join_not_rejected() {
		
		GroupJoin application = new GroupJoinFixture().get();
		Group group = new GroupFixture().get();
		new Expectations(group) {{
			groupJoinRepository.findById(new GroupJoinId("a01"));
				result = Optional.of(application);
			groupRepository.findById(new GroupId("g02"));
				result = Optional.of(group);
				
			group.reject(application);
				result = false;
		}};
		
		try {
			target.rejectJoin("g02", "a01");
		} catch (ApplicationException e) {
			assertThat(e.hasErrorOf(ErrorType.CANNOT_REJECT_JOIN), is(true));
		} 
	}

}
