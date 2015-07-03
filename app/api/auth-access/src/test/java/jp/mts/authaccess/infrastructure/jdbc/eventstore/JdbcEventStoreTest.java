package jp.mts.authaccess.infrastructure.jdbc.eventstore;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import jp.mts.authaccess.application.event.StoredEvent;
import jp.mts.authaccess.test.helper.Dates;
import jp.mts.authaccess.test.helper.JdbcTestBase;

import org.junit.Test;

public class JdbcEventStoreTest extends JdbcTestBase {

	@Test
	public void test_persistence() {
		JdbcEventStore target = new JdbcEventStore();
		target.add(new StoredEvent(Dates.dateTime("2016/01/15 12:01:02.003"), "test", "testbody".getBytes()));
		List<StoredEvent> storedEvents = target.findEventsAfter(0);
		
		assertThat(storedEvents.size(), is(1));
		assertThat(storedEvents.get(0).getOccurred(), is(Dates.dateTime("2016/01/15 12:01:02.003")));
		assertThat(storedEvents.get(0).getEventType(), is("test"));
		assertThat(new String(storedEvents.get(0).getEventBody()), is("testbody"));
	}
	
}
