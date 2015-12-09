package jp.mts.libs.event.eventstore;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import jp.mts.libs.event.Event;
import jp.mts.libs.event.EventDelegateType;
import jp.mts.libs.unittest.Dates;

import org.junit.Test;

public class StoredEventSerializerTest {


	@Test
	public void test() {
		StoredEventSerializer target = new StoredEventSerializer();
		
		StoredEvent serialized = target.serialize(new TestEvent());
		EventBody deserializeBody = target.deserializeBody(serialized);
		
		assertThat(deserializeBody.asString("testId.value"), is("id value"));
		assertThat(serialized.getEventType(), is("testevent"));
		assertThat(serialized.getOccurred(), is(Dates.date("2015/01/01")));
		assertThat(serialized.getPublisherId(), is("publisher01"));
	}
	
	
	public static class TestEvent implements Event{ 
		private Id testId = new Id("id value");

		@Override
		public Date occurred() {
			return Dates.date("2015/01/01");
		}

		@Override
		public String eventType() {
			return "testevent";
		}

		@Override
		public EventDelegateType eventDelegateType() {
			return EventDelegateType.EVENT_STORE;
		}

		@Override
		public String publisherId() {
			return "publisher01";
		}
	}
	
	public static class Id {
		private String value;

		public Id(String value) {
			this.value = value;
		}
	}

}
