package jp.mts.taskmanage.mq.listener;

import jp.mts.libs.event.mq.MqEventListener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//@Component
public class TaskManageEventListener extends MqEventListener {

	//@RabbitListener(queues="task-manage")
	public void proccess(Message message) {
		super.process(message);
	}
}
