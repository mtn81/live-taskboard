package jp.mts.base.infrastructure.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import jp.mts.libs.event.eventstore.StoredEvent;
import jp.mts.libs.unittest.Dates;

import org.junit.Test;

public class JdbcEventStoreTest {

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
