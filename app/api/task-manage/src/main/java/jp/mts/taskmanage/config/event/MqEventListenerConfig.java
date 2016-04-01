package jp.mts.taskmanage.config.event;

import jp.mts.base.config.AbstractMqEventListenerConfig;
import jp.mts.taskmanage.mq.listener.ChangeMemberSettingsEventHandler;
import jp.mts.taskmanage.mq.listener.CreateMemberEventHandler;
import jp.mts.taskmanage.mq.listener.GroupCreatedEventHandler;
import jp.mts.taskmanage.mq.listener.GroupJoinAcceptedEventHandler;
import jp.mts.taskmanage.mq.listener.GroupJoinApplicatedEventHandler;
import jp.mts.taskmanage.mq.listener.GroupJoinRejectedEventHandler;

import org.springframework.context.annotation.Configuration;

@AbstractMqEventListenerConfig.EndPoint(
	id="task-manage", 
	queueName="task-manage-event",  
	eventHandlers={
		CreateMemberEventHandler.class,
		GroupCreatedEventHandler.class,
		GroupJoinAcceptedEventHandler.class,
		ChangeMemberSettingsEventHandler.class,
		GroupJoinApplicatedEventHandler.class,
		GroupJoinAcceptedEventHandler.class,
		GroupJoinRejectedEventHandler.class, 
	}
)
@Configuration
public class MqEventListenerConfig extends AbstractMqEventListenerConfig {}
