package jp.mts.taskmanage.application;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import jp.mts.taskmanage.domain.model.auth.MemberAuth;
import jp.mts.taskmanage.domain.model.auth.MemberAuthService;
import jp.mts.taskmanage.domain.model.member.MemberId;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.junit.Test;

public class MemberAuthAppServiceTest {

	@Tested MemberAuthAppService target;
	@Injectable MemberAuthService memberAuthService;
	
	@Test
	public void test_successWhenValidMemberAuthExists() {
		MemberAuth memberAuth = new MemberAuth(new MemberId("a01"));
		new Expectations(memberAuth) {{
			memberAuth.isExpired(); 
				result = false;
			memberAuthService.establishAuth("a01");
				result = Optional.of(memberAuth);
		}};
		
		MemberAuth actual = target.validateAuth("a01");
		assertThat(actual.isExpired(), is(false));
	}
	@Test
	public void test_failedWhenMemberAuthNotExsist() {
		new Expectations() {{
			memberAuthService.establishAuth("a01");
				result = Optional.empty();
		}};
		
		MemberAuth actual = target.validateAuth("a01");
		assertThat(actual, is(nullValue()));
	}
	@Test
	public void test_failedWhenMemberAuthExpired() {
		MemberAuth memberAuth = new MemberAuth(new MemberId("a01"));
		new Expectations(memberAuth) {{
			memberAuth.isExpired(); 
				result = true;
			memberAuthService.establishAuth("a01");
				result = Optional.of(memberAuth);
		}};
		
		MemberAuth actual = target.validateAuth("a01");
		assertThat(actual.isExpired(), is(true));
	}

}
