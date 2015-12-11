package jp.mts.taskmanage;

import jp.mts.libs.event.mq.MqEventListener;
import jp.mts.taskmanage.mq.listener.CreateMemberEventHandler;
import jp.mts.taskmanage.mq.listener.GroupCreatedEventHandler;
import jp.mts.taskmanage.mq.listener.MemberJoinAcceptedEventHandler;
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
	private CreateMemberEventHandler memberCreatedEventHandler;
	@Autowired
	private GroupCreatedEventHandler groupCreatedEventHandler;
	@Autowired
	private MemberJoinAcceptedEventHandler memberJoinAcceptedEventHandler;
	@Autowired
	private TaskNotifyWebSocketController taskNotifyWebSocketController;
	
	@Override
	public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
		MqEventListener listener = new MqEventListener(
				memberCreatedEventHandler,
				groupNotifyController,
				groupCreatedEventHandler,
				memberJoinAcceptedEventHandler,
				taskNotifyWebSocketController);
		SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
		endpoint.setId("task-manage");
		endpoint.setQueueNames("task-manage");
		endpoint.setMessageListener(listener::process);
		registrar.registerEndpoint(endpoint);
	}
}
