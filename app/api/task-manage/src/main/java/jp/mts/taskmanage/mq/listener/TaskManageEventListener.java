package jp.mts.taskmanage.mq.listener;

import jp.mts.libs.event.mq.MqEventListener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class TaskManageEventListener extends MqEventListener {

	@Autowired
	private GroupCreatedEventHandler groupCreatedEventHandler;
	@Autowired
	private MemberCreatedEventHandler memberCreatedEventHandler;
	
	@RabbitListener(queues="task-manage")
	public void proccess(Message message) {
		processTemplate(message, Lists.newArrayList(
				groupCreatedEventHandler, 
				memberCreatedEventHandler));
	}
}
