package jp.mts.taskmanage.mq.listener;

import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.eventstore.StoredEventSerializer;
import jp.mts.libs.unittest.Dates;
import jp.mts.libs.unittest.Maps;
import jp.mts.taskmanage.application.GroupAppService;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.junit.Test;


public class GroupCreatedEventListenerTest {
	
	@Tested GroupCreatedEventListener target = new GroupCreatedEventListener();
	@Injectable GroupAppService groupAppService;
	@Injectable StoredEventSerializer storedEventSerializer;

	@Test
	public void test() {
		
		EventBody eventBody = new EventBody(new Maps()
			.e("groupId", Maps.map("value", "g01"))
			.e("creator", Maps.map("value", "m01"))
			.get());
		
		new Expectations() {{
			groupAppService.entryGroupAsAdministrator("g01", "m01");
			groupAppService.changeGroupToAvailable("g01");
		}};
		
		target.doProccess(1, Dates.dateTime("2015/07/20 12:00:00.000"), eventBody);
		
	}

}
