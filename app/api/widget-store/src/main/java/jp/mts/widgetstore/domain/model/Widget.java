package jp.mts.widgetstore.domain.model;

import jp.mts.base.domain.model.DomainEntity;

public class Widget extends DomainEntity<WidgetId>{
	
	private Position position;
	private Size size;

	public Widget(WidgetId widgetId) {
		super(widgetId);
	}

	public WidgetId widgetId() {
		return id();
	}
	public Position position() {
		return position;
	}
	public Size size(){
		return size;
	}
	
	public void change(Position position, Size size) {
		setPosition(position);
		setSize(size);
		
		domainEventPublisher.publish(
				new WidgetChanged(widgetId(), position, size));
	}

	void setPosition(Position position) {
		this.position = position;
	}
	void setSize(Size size){
		this.size = size;
	}

}
