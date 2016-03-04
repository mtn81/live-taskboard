package jp.mts.taskmanage.config.event;

import jp.mts.libs.event.mq.MqEventListener;
import jp.mts.taskmanage.mq.listener.ChangeMemberSettingsEventHandler;
import jp.mts.taskmanage.mq.listener.CreateMemberEventHandler;
import jp.mts.taskmanage.mq.listener.GroupCreatedEventHandler;
import jp.mts.taskmanage.mq.listener.MemberJoinAcceptedEventHandler;

import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqEventListenerConfig implements RabbitListenerConfigurer {
	
	@Autowired
	private CreateMemberEventHandler memberCreatedEventHandler;
	@Autowired
	private GroupCreatedEventHandler groupCreatedEventHandler;
	@Autowired
	private MemberJoinAcceptedEventHandler memberJoinAcceptedEventHandler;
	@Autowired
	private ChangeMemberSettingsEventHandler changeMemberSettingsEventHandler;
	
	@Override
	public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
		MqEventListener listener = new MqEventListener(
				memberCreatedEventHandler,
				groupCreatedEventHandler,
				memberJoinAcceptedEventHandler,
				changeMemberSettingsEventHandler);
		SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
		endpoint.setId("task-manage");
		endpoint.setQueueNames("task-manage-event");
		endpoint.setMessageListener(listener::process);
		registrar.registerEndpoint(endpoint);
	}
}
