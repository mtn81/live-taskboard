package jp.mts.taskmanage.rest.aspect;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.taskmanage.application.MemberAppService;
import jp.mts.taskmanage.domain.model.member.MemberFixture;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

public class MemberContextTest {

	@Mocked MemberAppService memberAppService;
	
	@Test
	public void test_hasMemerId() {
		
		MemberContext.start("m01");
		
		boolean actual = MemberContext.hasMemberId("m01");

		assertThat(actual, is(true));
	}

	@Test
	public void test_isGroupAdmin() {
		MemberContext.setMemberAppService(memberAppService);
		
		new Expectations() {{
			memberAppService.findById("m01");
				result = new MemberFixture("m01")
						.addGroupBelonging("g01", true)
						.addGroupBelonging("g02", false)
						.get();
				times = 1; //enable cache
		}};
		
		MemberContext.start("m01");
		
		assertThat(MemberContext.isGroupAdmin("g01"), is(true));
		assertThat(MemberContext.isGroupAdmin("g02"), is(false));
	}
	@Test
	public void test_belongsGroup() {
		MemberContext.setMemberAppService(memberAppService);
		
		new Expectations() {{
			memberAppService.findById("m01");
				result = new MemberFixture("m01")
						.addGroupBelonging("g01", true)
						.addGroupBelonging("g02", false)
						.get();
				times = 1; //enable cache
		}};
		
		MemberContext.start("m01");
		
		assertThat(MemberContext.belongs("g01"), is(true));
		assertThat(MemberContext.belongs("g02"), is(true));
	}

}
