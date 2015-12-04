package jp.mts.widgetstore.websocket;

import java.util.Date;

import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.mq.MqEventHandler;
import jp.mts.libs.event.mq.MqEventHandlerConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@MqEventHandlerConfig(targetEventTypes="mts:widgetstore/WidgetChanged")
public class WidgetChangeNotifyWebSocketController implements MqEventHandler {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@Override
	public void handleEvent(long eventId, Date occurred, EventBody eventBody) {
		String widgetId = eventBody.asString("widgetId.value");
		String categoryId = eventBody.asString("categoryId.value");

		simpMessagingTemplate.convertAndSend(
				"/topic/" + categoryId + "/widget_changed", 
				"widgetChanged");
	}
}
