package jp.mts.widgetstore.domain.model;

public class WidgetFixture {

	private Widget widget;
	
	public WidgetFixture(String categoryId, String widgetId) {
		this(new WidgetId(categoryId, widgetId), new Position(10, 20), new Size(100, 200));
	}
	public WidgetFixture(WidgetId widgetId) {
		this(widgetId, new Position(10, 20), new Size(100, 200));
	}
	public WidgetFixture(WidgetId widgetId, Position position, Size size) {
		widget = new Widget(widgetId);
		widget.setPosition(position);
		widget.setPosition(position);
	}
	
	public Widget get(){
		return widget;
	}
}
