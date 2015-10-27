package jp.mts.authaccess.mq.listener;

import jp.mts.libs.event.mq.MqEventListener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

//@Component
public class AuthAccessEventListener extends MqEventListener {

	@Autowired
	private UserRegisteredEventHandler userRegisteredEventHandler;
	
//	@RabbitListener(queues="auth-access")
	public void proccess(Message message) {
//		process(message, Lists.newArrayList(
//				userRegisteredEventHandler));
	}
}
