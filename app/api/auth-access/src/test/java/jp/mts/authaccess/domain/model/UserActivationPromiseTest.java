package jp.mts.authaccess.domain.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.base.domain.model.DomainCalendar;
import jp.mts.base.domain.model.DomainObject;
import jp.mts.libs.unittest.Dates;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Before;
import org.junit.Test;

public class UserActivationPromiseTest {

	UserActivationPromise target;
	@Mocked DomainCalendar domainCalendar;
	
	@Before
	public void setup() {
		target = new UserActivationPromiseFixture()
			.setExpireTime(Dates.dateShortTime("2015/10/01 12:00")).get();

		DomainObject.setDomainCalendar(domainCalendar);
	}

	@Test
	public void test_isExpired() {
		
		new Expectations() {{
			domainCalendar.systemDate();
				result = Dates.dateTime("2015/10/01 12:00:01.000");
		}};
		
		assertThat(target.isExpired(), is(true));
	}

	@Test
	public void test_isNotExpired() {
		
		new Expectations() {{
			domainCalendar.systemDate();
				result = Dates.dateTime("2015/10/01 12:00:00.000");
		}};
		
		assertThat(target.isExpired(), is(false));
	}
}
