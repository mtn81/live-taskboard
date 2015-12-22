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

	public String getWidgetCategoryId() {
		return widgetId.categoryId();
	}
	public String getWidgetId() {
		return widgetId.widgetId();
	}
	public int getPositionX() {
		return position.x();
	}
	public int getPositionY() {
		return position.y();
	}
	public int getWidth() {
		return size.width();
	}
	public int getHeight() {
		return size.height();
	}
}
