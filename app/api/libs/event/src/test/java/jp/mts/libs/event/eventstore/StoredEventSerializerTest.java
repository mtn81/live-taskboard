package jp.mts.libs.event.eventstore;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import jp.mts.libs.event.Event;

import org.junit.Test;

public class StoredEventSerializerTest {


	@Test
	public void test() {
		StoredEventSerializer target = new StoredEventSerializer();
		
		StoredEvent serialized = target.serialize(new TestEvent());
		EventBody deserializeBody = target.deserializeBody(serialized);
		
		assertThat(deserializeBody.asString("testId.value"), is("id value"));
	}
	
	
	public static class TestEvent implements Event{ 
		private Id testId = new Id("id value");

		@Override
		public Date occurred() {
			return new Date();
		}

		@Override
		public String eventType() {
			return "testevent";
		}
	}
	
	public static class Id {
		private String value;

		public Id(String value) {
			this.value = value;
		}
	}

}
