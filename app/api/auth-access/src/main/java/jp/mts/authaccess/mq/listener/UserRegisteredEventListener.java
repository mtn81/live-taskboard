package jp.mts.authaccess.mq.listener;

import jp.mts.authaccess.application.UserAppService;
import jp.mts.authaccess.domain.model.UserActivation;
import jp.mts.libs.event.mq.EventMqListener;
import jp.mts.libs.event.mq.EventMqListenerConfig;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@EventMqListenerConfig(targetEventTypes={"jp.mts.authaccess.domain.model.UserRegistered"})
public class UserRegisteredEventListener extends EventMqListener {

	@Autowired
	private UserAppService userAppService;
	
	@RabbitListener(queues="auth-access")
	public void proccess(Message message) {
		processTemplate(message, (eventId, occurred, eventBody) -> {
			UserActivation userActivationPromise 
				= userAppService.prepareActivation(eventBody.asString("userId.value"));
			System.out.println(userActivationPromise);
			//TODO send mail
		});
	}
	
}
