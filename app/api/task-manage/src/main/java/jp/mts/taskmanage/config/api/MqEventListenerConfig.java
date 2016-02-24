package jp.mts.taskmanage.config.api;

import jp.mts.libs.event.mq.MqEventListener;
import jp.mts.taskmanage.websocket.GroupNotifyWebSocketController;
import jp.mts.taskmanage.websocket.TaskNotifyWebSocketController;

import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqEventListenerConfig implements RabbitListenerConfigurer {
	
	@Autowired
	private GroupNotifyWebSocketController groupNotifyController;
	@Autowired
	private TaskNotifyWebSocketController taskNotifyWebSocketController;
	
	@Override
	public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
		MqEventListener listener = new MqEventListener(
				groupNotifyController,
				taskNotifyWebSocketController);
		SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
		endpoint.setId("task-manage");
		endpoint.setQueueNames("task-manage-api");
		endpoint.setMessageListener(listener::process);
		registrar.registerEndpoint(endpoint);
	}
}
