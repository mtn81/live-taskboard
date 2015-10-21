package jp.mts.taskmanage.mq.listener;

import jp.mts.libs.event.mq.EventMqListener;
import jp.mts.libs.event.mq.EventMqListenerConfig;
import jp.mts.taskmanage.application.GroupAppService;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@EventMqListenerConfig(targetEventTypes={"jp.mts.taskmanage.domain.model.GroupCreated"})
public class GroupCreatedEventListener extends EventMqListener {

	@Autowired
	private GroupAppService groupAppService;
	
	@RabbitListener(queues="task-manage")
	public void proccess(Message message) {
		processTemplate(message, (eventId, occurred, eventBody) -> {
			groupAppService.entryGroupAsAdministrator(
					eventBody.asString("groupId.value"), 
					eventBody.asString("creator.value"));
			groupAppService.changeGroupToAvailable(
					eventBody.asString("groupId.value"));
		});
	}
	
}
