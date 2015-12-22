package jp.mts.authaccess.mq.listener;

import java.util.Date;

import jp.mts.authaccess.application.UserAppService;
import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.mq.MqEventHandler;
import jp.mts.libs.event.mq.MqEventHandlerConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@MqEventHandlerConfig(targetEventTypes={"mts:authaccess/UserRegistered"})
public class UserRegisteredEventHandler implements MqEventHandler {

	@Autowired
	private UserAppService userAppService;
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Override
	public void handleEvent(
			long eventId, String publisherId, Date occurred, EventBody eventBody) {
		String activationIdValue = eventBody.asString("activationId");
		String email = eventBody.asString("email");
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(email);
		mailMessage.setText("http://192.168.77.11:9000/#/activate/" + activationIdValue);
		javaMailSender.send(mailMessage);
	}
	
}
