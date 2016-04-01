package jp.mts.widgetstore.config.api;

import jp.mts.base.config.AbstractMqEventListenerConfig;
import jp.mts.widgetstore.websocket.WidgetChangeNotifyWebSocketController;

@AbstractMqEventListenerConfig.EndPoint(
	id="widget-store", 
	queueName="widget-store",  
	eventHandlers={
		WidgetChangeNotifyWebSocketController.class,
	}
)
public class MqEventListenerConfig extends AbstractMqEventListenerConfig {}
