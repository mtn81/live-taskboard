package jp.mts.widgetstore.rest.presentation.model;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import jp.mts.widgetstore.application.WidgetAppService;
import jp.mts.widgetstore.domain.model.Widget;

public class WidgetList {

	//input

	//output
	private List<Widget> widgets = new ArrayList<>();
	
	public List<WidgetView> getWidgets(){
		return widgets.stream().map(w -> new WidgetView(w)).collect(toList());
	}
	
	//process
	public void loadByCategory(String categoryId, WidgetAppService widgetAppService) {
		List<Widget> widgets = widgetAppService.findWidgetsInCategory(categoryId);
		this.widgets = widgets;
		
	}
	
	public static class WidgetView {
		
		private Widget widget;
		
		public WidgetView(Widget widget) {
			this.widget = widget;
		}

		public String getCategoryId() {
			return widget.widgetId().categoryId();
		}
		public String getWidgetId() {
			return widget.widgetId().widgetId();
		}
		public Integer getWidth() {
			return widget.size().width();
		}
		public Integer getHeight() {
			return widget.size().height();
		}
		public Integer getLeft() {
			return widget.position().x();
		}
		public Integer getTop() {
			return widget.position().y();
		}
	}

}
