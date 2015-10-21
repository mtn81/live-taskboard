package jp.mts.authaccess.mq.listener;

import javax.mail.internet.MimeMessage;


import jp.mts.authaccess.application.UserAppService;
import jp.mts.authaccess.domain.model.UserActivation;
import jp.mts.libs.event.mq.EventMqListener;
import jp.mts.libs.event.mq.EventMqListenerConfig;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@EventMqListenerConfig(targetEventTypes={"jp.mts.authaccess.domain.model.UserRegistered"})
public class UserRegisteredEventListener extends EventMqListener {

	@Autowired
	private UserAppService userAppService;
	@Autowired
	private JavaMailSender javaMailSender;
	
	@RabbitListener(queues="auth-access")
	public void proccess(Message message) {
		processTemplate(message, (eventId, occurred, eventBody) -> {
			
			String userIdValue = eventBody.asString("userId.value");
			String email = eventBody.asString("email");
			
			UserActivation userActivationPromise 
				= userAppService.prepareActivation(userIdValue);
			
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(email);
			mailMessage.setText("http://192.168.77.11:9000/#/activate/" + userActivationPromise.id().value());
			javaMailSender.send(mailMessage);
			
		});
	}
	
}
