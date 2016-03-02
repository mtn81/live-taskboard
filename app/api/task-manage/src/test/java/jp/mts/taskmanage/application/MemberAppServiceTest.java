package jp.mts.taskmanage.application;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.MemberFixture;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.domain.model.MemberRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.junit.Test;

public class MemberAppServiceTest {

	@Tested MemberAppService memberAppService;
	@Injectable MemberRepository memberRepository;

	@Test public void 
	test_findMembersInGroup() {
		
		List<Member> expected = new ArrayList<>();
		new Expectations() {{
			memberRepository.findByGroupId(new GroupId("g01"));
				result = expected;
		}};

		List<Member> actual = memberAppService.findMembersInGroup("g01");
		
		assertThat(actual, is(expected));
	}
	
	@Test public void
	test_changeMember() {
		
		Member foundMember = new MemberFixture().get();
		new Expectations(foundMember) {{
			memberRepository.findById(new MemberId("m01"));
				result = Optional.of(foundMember);
				
			foundMember.changeAttributes("taro", "taro@test.jp");
			
			memberRepository.save(foundMember);
		}};
		
		memberAppService.changeMember(
				"m01", 
				"taro", 
				"taro@test.jp");
	}

}
