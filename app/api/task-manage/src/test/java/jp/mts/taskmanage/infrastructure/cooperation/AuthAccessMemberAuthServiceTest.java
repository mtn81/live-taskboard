package jp.mts.taskmanage.infrastructure.cooperation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;

import jp.mts.base.domain.model.DomainCalendar;
import jp.mts.base.domain.model.DomainObject;
import jp.mts.libs.unittest.Dates;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.domain.model.auth.MemberAuth;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.mashape.unirest.http.JsonNode;

public class AuthAccessMemberAuthServiceTest {

	AuthAccessMemberAuthService target;
	@Mocked AuthApi authApi;
	@Mocked AuthAccessConverter authAccessConverter;
	@Mocked JSONObject responseBody;
	@Mocked DomainCalendar domainCalendar;
	
	@Before
	public void setup() {
		target = new AuthAccessMemberAuthService();
		Deencapsulation.setField(target, authApi);
		Deencapsulation.setField(target, authAccessConverter);
		DomainObject.setDomainCalendar(domainCalendar);
		MemberAuth.setExpirationMinutes(60);
		new Expectations() {{
			domainCalendar.systemDate();
				result = Dates.dateTime("2015/10/01 12:00:00.000");
		}};
	}

	@Test
	public void test_loadAuthApiResultWhenCacheNotFound() {
		MemberAuth memberAuth = new MemberAuth(new MemberId("m01"));
		new Expectations() {{
			authApi.loadAuth("a01");
				result = responseBody;
			authAccessConverter.toMemberAuth(responseBody);
				result = memberAuth;
		}};
		
		Optional<MemberAuth> actual = target.establishAuth("a01");
		assertThat(actual.get().memberId(), is(new MemberId("m01")));
		assertThat(actual.get().expireTime(), is(Dates.dateTime("2015/10/01 13:00:00.000")));
	}
	@Test
	public void test_キャッシュの有効期限切れの時は有効期限がのびない() {
		MemberAuth memberAuth = new MemberAuth(new MemberId("m01"));
		
		new Expectations() {{
			authApi.loadAuth("a01");
				result = responseBody;
				times = 1;
			authAccessConverter.toMemberAuth(responseBody);
				result = memberAuth;
				times = 1;
		}};
		
		Optional<MemberAuth> apiCallResult = target.establishAuth("a01");
		assertThat(apiCallResult.get().expireTime(), is(Dates.dateTime("2015/10/01 13:00:00.000")));

		new Expectations() {{
			domainCalendar.systemDate();
				result = Dates.dateTime("2015/10/01 14:00:00.000");
		}};
		Optional<MemberAuth> cacheResult = target.establishAuth("a01");
		assertThat(cacheResult.get().expireTime(), is(Dates.dateTime("2015/10/01 13:00:00.000")));
	}

	@Test
	public void test_キャッシュの有効期限以内れの時は有効期限がのびる() {
		MemberAuth memberAuth = new MemberAuth(new MemberId("m01"));
		
		new Expectations() {{
			authApi.loadAuth("a01");
				result = responseBody;
				times = 1;
			authAccessConverter.toMemberAuth(responseBody);
				result = memberAuth;
				times = 1;
		}};
		
		Optional<MemberAuth> apiCallResult = target.establishAuth("a01");
		assertThat(apiCallResult.get().expireTime(), is(Dates.dateTime("2015/10/01 13:00:00.000")));

		new Expectations() {{
			domainCalendar.systemDate();
				result = Dates.dateTime("2015/10/01 12:30:00.000");
		}};
		Optional<MemberAuth> cacheResult = target.establishAuth("a01");
		assertThat(cacheResult.get().expireTime(), is(Dates.dateTime("2015/10/01 13:30:00.000")));
	}
}
