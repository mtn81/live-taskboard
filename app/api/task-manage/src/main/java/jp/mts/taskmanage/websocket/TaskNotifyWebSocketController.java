package jp.mts.taskmanage.websocket;

import java.util.Date;

import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.mq.MqEventHandler;
import jp.mts.libs.event.mq.MqEventHandlerConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@MqEventHandlerConfig(targetEventTypes={
	"mts:taskmanage/TaskRegistered",
	"mts:taskmanage/TaskModified",
	"mts:taskmanage/TaskRemoved"
})
public class TaskNotifyWebSocketController implements MqEventHandler {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Override
	public void handleEvent(
			long eventId, String publisherId, Date occurred, EventBody eventBody) {
		String groupId = eventBody.asString("groupId.value");

		simpMessagingTemplate.convertAndSend(
				"/topic/" + groupId + "/task_changed", 
				new TaskChangeNotification(publisherId));
	}
	
	public static class TaskChangeNotification {
		public String clientId;

		public TaskChangeNotification(String clientId) {
			this.clientId = clientId;
		}
	}
}
