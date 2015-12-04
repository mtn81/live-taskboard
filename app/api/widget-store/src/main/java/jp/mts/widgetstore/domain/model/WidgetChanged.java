package jp.mts.widgetstore.domain.model;

import static jp.mts.libs.event.EventDelegateType.DIRECT;
import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;

@DomainEventConfig(
	eventType="mts:widgetstore/WidgetChanged",
	delegateType=DIRECT)
public class WidgetChanged extends DomainEvent {

	private WidgetId widgetId;
	private Position position;
	private Size size;

	public WidgetChanged(WidgetId widgetId, Position position, Size size) {
		this.widgetId = widgetId;
		this.position = position;
		this.size = size;
	}

	public WidgetId getWidgetId() {
		return widgetId;
	}
	public Position getPosition() {
		return position;
	}
	public Size getSize() {
		return size;
	}
}
