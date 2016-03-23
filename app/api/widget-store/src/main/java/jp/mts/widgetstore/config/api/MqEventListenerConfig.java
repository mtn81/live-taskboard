package jp.mts.widgetstore.config.api;

import jp.mts.libs.event.mq.MqEventListener;
import jp.mts.widgetstore.websocket.WidgetChangeNotifyWebSocketController;

import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqEventListenerConfig implements RabbitListenerConfigurer {
	
	@Autowired
	private WidgetChangeNotifyWebSocketController widgetChangeNotifyWebSocketController;
	
	@Override
	public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
		MqEventListener listener = new MqEventListener(
				widgetChangeNotifyWebSocketController);
		SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
		endpoint.setId("widget-store");
		endpoint.setQueueNames("widget-store");
		endpoint.setMessageListener(listener::process);
		registrar.registerEndpoint(endpoint);
	}
}
