package jp.mts.widgetstore.domain.model;

public class Widget {
	
	private WidgetId widgetId;
	private Position position;
	private Size size;

	public Widget(WidgetId widgetId, Position position, Size size) {
		this.widgetId = widgetId;
		this.position = position;
		this.size = size;
	}

	public WidgetId widgetId() {
		return widgetId;
	}
	public Position position() {
		return position;
	}
	public Size size(){
		return size;
	}

	void setPosition(Position position) {
		this.position = position;
	}
	void setSize(Size size){
		this.size = size;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((widgetId == null) ? 0 : widgetId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Widget other = (Widget) obj;
		if (widgetId == null) {
			if (other.widgetId != null)
				return false;
		} else if (!widgetId.equals(other.widgetId))
			return false;
		return true;
	}
	
}
