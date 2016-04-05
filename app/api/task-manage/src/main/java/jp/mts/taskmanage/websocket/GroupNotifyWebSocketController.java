package jp.mts.taskmanage.websocket;

import java.util.Date;

import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.mq.MqEventHandler;
import jp.mts.libs.event.mq.MqEventHandlerConfig;
import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.domain.model.group.Group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@MqEventHandlerConfig(targetEventTypes="mts:taskmanage/GroupMemberEntried")
public class GroupNotifyWebSocketController implements MqEventHandler {

	@Autowired
	private GroupAppService groupAppService;
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@Override
	public void handleEvent(
			long eventId, String publisherId, Date occurred, EventBody eventBody) {
		String groupId = eventBody.asString("groupId");
		String memberId = eventBody.asString("memberId");
		
		Group group = groupAppService.findBelongingGroup(groupId, memberId);
		if (group != null) {
			simpMessagingTemplate.convertAndSend(
				"/topic/" + memberId + "/group_available", 
				new GroupAvailableNotification(group));
		}
	}
}
