package jp.mts.base.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.stream.Collectors;

import jp.mts.libs.event.mq.MqEventHandler;
import jp.mts.libs.event.mq.MqEventListener;

import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class AbstractMqEventListenerConfig implements RabbitListenerConfigurer {

	@Autowired
	private ApplicationContext context;
	
	private EndPoint endPoint;
	
	protected AbstractMqEventListenerConfig() {
		endPoint = this.getClass().getSuperclass().getAnnotation(EndPoint.class);
	}

	@Override
	public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
		MqEventListener listener = new MqEventListener(Arrays.stream(endPoint.eventHandlers())
				.map(h -> context.getBean(h))
				.collect(Collectors.toList()));
		SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
		endpoint.setId(endPoint.id());
		endpoint.setQueueNames(endPoint.queueName());
		endpoint.setMessageListener(listener::process);
		registrar.registerEndpoint(endpoint);
	}
	
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public static @interface EndPoint {
		String id();
		String queueName();
		Class<? extends MqEventHandler>[] eventHandlers();
	}
	
}
