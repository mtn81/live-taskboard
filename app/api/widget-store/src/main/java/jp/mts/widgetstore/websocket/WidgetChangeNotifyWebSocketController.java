package jp.mts.widgetstore.websocket;

import java.util.Date;
import java.util.List;

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
	public void handleEvent(
			long eventId, String publisherId, Date occurred, EventBody eventBody) {
		List<String> widgetIds = eventBody.as(List.class, "widgetId.value");

		String categoryId = widgetIds.get(0);

		simpMessagingTemplate.convertAndSend(
				"/topic/" + categoryId + "/widget_changed", 
				new WidgetChanged(publisherId));
	}
	
	public static class WidgetChanged {
		public String clientId;

		public WidgetChanged(
				String clientId) {
			this.clientId = clientId;
		}
	}
}
