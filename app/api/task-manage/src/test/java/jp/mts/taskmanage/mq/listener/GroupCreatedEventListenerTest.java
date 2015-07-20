package jp.mts.taskmanage.mq.listener;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.eventstore.StoredEventSerializer;
import jp.mts.libs.unittest.Dates;
import jp.mts.libs.unittest.Maps;
import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.MemberId;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;

import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.NEW;


public class GroupCreatedEventListenerTest {
	
	@Tested GroupCreatedEventListener target = new GroupCreatedEventListener();
	@Injectable GroupAppService groupAppService;
	@Injectable StoredEventSerializer storedEventSerializer;

	@Test
	public void test() {
		
		final GroupId groupId = new GroupId("g01");
		final MemberId memberId = new MemberId("m01");
		
		EventBody eventBody = new EventBody(new Maps()
			.e("groupId", Maps.map("value", "g01"))
			.e("creator", Maps.map("value", "m01"))
			.get());
		
		new Expectations() {{
			groupAppService.entryMember(groupId, memberId);
		}};
		
		target.doProccess(1, Dates.dateTime("2015/07/20 12:00:00.000"), eventBody);
		
	}

}
