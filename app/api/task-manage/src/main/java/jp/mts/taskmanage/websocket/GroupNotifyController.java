package jp.mts.taskmanage.websocket;

import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.domain.model.Group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GroupNotifyController {

	@Autowired
	private GroupAppService groupAppService;
	
	@MessageMapping(value="/groups/{groupId}/watch_available")
	@SendToUser(value="/queue/group_available", broadcast=false)
	public GroupAvailableNotification notifyGroupAvailable(
			@DestinationVariable String groupId){
		Group group = groupAppService.detectRegisteredGroupAvailable(groupId);
		return new GroupAvailableNotification(group);
	}

}
