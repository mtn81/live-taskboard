package jp.mts.widgetstore.domain.model;

public class WidgetBuilder {

	private Widget target;

	public WidgetBuilder(Widget target) {
		this.target = target;
	}
	
	public WidgetBuilder position(Position position) {
		target.setPosition(position);
		return this;
	}
	
	public WidgetBuilder size(Size size) {
		target.setSize(size);
		return this;
	}
	
	public Widget get() {
		return target;
	}
	
}
