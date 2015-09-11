package jp.mts.taskmanage.application;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.MemberRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;

import org.junit.Test;

public class MemberAppServiceTest {

	@Tested MemberAppService memberAppService;
	@Injectable MemberRepository memberRepository;

	@Test
	public void test() {
		
		List<Member> expected = new ArrayList<>();
		new Expectations() {{
			memberRepository.findByGroupId(new GroupId("g01"));
				result = expected;
		}};

		List<Member> actual = memberAppService.findMembersInGroup("g01");
		
		assertThat(actual, is(expected));
	}

}
