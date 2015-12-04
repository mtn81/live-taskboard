package jp.mts.widgetstore.rest.presentation.model;

import jp.mts.widgetstore.application.WidgetAppService;
import jp.mts.widgetstore.domain.model.Position;
import jp.mts.widgetstore.domain.model.Size;
import jp.mts.widgetstore.domain.model.WidgetId;

public class WidgetSave {
	
	// input
	private int left;
	private int top;
	private int width;
	private int height;
	
	public void setLeft(int left) {
		this.left = left;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	//output
	private WidgetId _widgetId;
	
	public String getWidgetId() {
		return _widgetId.widgetId();
	}
	public String getCategoryId() {
		return _widgetId.categoryId();
	}
	
	//proccess
	public void save(
			String categoryId, 
			String widgetId,
			WidgetAppService widgetAppService) {

		WidgetId _widgetId = new WidgetId(categoryId, widgetId);
		widgetAppService.changeGraphic(
				_widgetId,
				new Position(left, top),
				new Size(width, height));

		this._widgetId = _widgetId;
	}
	
}
