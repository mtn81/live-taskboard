package jp.mts.widgetstore.infrastructure.cooperation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.base.domain.model.DomainCalendar;
import jp.mts.base.domain.model.DomainObject;
import jp.mts.libs.unittest.Dates;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;

import org.junit.Test;

public class TaskManageWidgetAuthDomainServiceTest {
	
	@Tested
	TaskManageWidgetAuthDomainService target;

	@Injectable
	TaskManageApi taskManageApi;


	@Test public void 
	test_taskManageApi_call() {
		
		new Expectations() {{
			taskManageApi.loadGroup("a01","c01");
				result = "c01";
		}};
		
		boolean actual = target.isAvailable("a01", "c01");
		
		assertThat(actual, is(true));
	}
	@Test public void 
	test_taskManageApi_not_called_by_cache(@Mocked DomainCalendar calender) {
		DomainObject.setDomainCalendar(calender);

		new Expectations() {{
			calender.systemDate();
				result = Dates.dateShortTime("2016/03/01 12:00");
				result = Dates.dateShortTime("2016/03/01 12:05");
			taskManageApi.loadGroup("a01","c01");
				result = "c01";
				times = 1;
		}};
		target.isAvailable("a01", "c01");
		boolean actual = target.isAvailable("a01", "c01");

		assertThat(actual, is(true));
	}
	@Test public void 
	test_taskManageApi_called_when_cache_expired(@Mocked DomainCalendar calender) {
		DomainObject.setDomainCalendar(calender);

		new Expectations() {{
			calender.systemDate();
				result = Dates.dateShortTime("2016/03/01 12:00");
				result = Dates.dateShortTime("2016/03/01 12:06");
			taskManageApi.loadGroup("a01","c01");
				result = "c01";
				times = 2;
		}};
		target.isAvailable("a01", "c01");
		boolean actual = target.isAvailable("a01", "c01");

		assertThat(actual, is(true));
	}


}
