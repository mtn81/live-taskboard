package jp.mts.taskmanage.config.api;

import jp.mts.base.config.AbstractMqEventListenerConfig;
import jp.mts.taskmanage.websocket.GroupNotifyWebSocketController;
import jp.mts.taskmanage.websocket.TaskNotifyWebSocketController;

import org.springframework.context.annotation.Configuration;

@AbstractMqEventListenerConfig.EndPoint(
	id="task-manage", 
	queueName="task-manage-api",  
	eventHandlers={
		GroupNotifyWebSocketController.class,
		TaskNotifyWebSocketController.class,
	}
)
@Configuration
public class MqEventListenerConfig extends AbstractMqEventListenerConfig {}
