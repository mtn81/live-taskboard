package jp.mts.taskmanage.rest.aspect;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.libs.unittest.Maps;
import jp.mts.taskmanage.application.query.MemberAuthorizationQuery;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

public class MemberContextTest {

	@Mocked MemberAuthorizationQuery memberAuthorizationQuery;
	
	@Test
	public void test_hasMemerId() {
		
		MemberContext.start("m01");
		
		boolean actual = MemberContext.hasMemberId("m01");

		assertThat(actual, is(true));
	}

	@Test
	public void test_isGroupAdmin() {
		MemberContext.setMemberAuthorizationQuery(memberAuthorizationQuery);
		
		new Expectations() {{
			memberAuthorizationQuery.belongingByMember("m01");
				result = new Maps().e("g01", true).e("g02", false).get();
				times = 1; //enable cache
		}};
		
		MemberContext.start("m01");
		
		assertThat(MemberContext.isGroupAdmin("g01"), is(true));
		assertThat(MemberContext.isGroupAdmin("g02"), is(false));
	}
	@Test
	public void test_belongsGroup() {
		MemberContext.setMemberAuthorizationQuery(memberAuthorizationQuery);
		
		new Expectations() {{
			memberAuthorizationQuery.belongingByMember("m01");
				result = new Maps().e("g01", true).e("g02", false).get();
				times = 1; //enable cache
		}};
		
		MemberContext.start("m01");
		
		assertThat(MemberContext.belongs("g01"), is(true));
		assertThat(MemberContext.belongs("g02"), is(true));
	}

}
