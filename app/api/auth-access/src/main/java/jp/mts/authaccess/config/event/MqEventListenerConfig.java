package jp.mts.authaccess.config.event;

import jp.mts.authaccess.mq.listener.UserRegisteredEventHandler;
import jp.mts.base.config.AbstractMqEventListenerConfig;

import org.springframework.context.annotation.Configuration;

@AbstractMqEventListenerConfig.EndPoint(
	id="auth-access", 
	queueName="auth-access",  
	eventHandlers={
		UserRegisteredEventHandler.class,
	}
)
@Configuration
public class MqEventListenerConfig extends AbstractMqEventListenerConfig {}
