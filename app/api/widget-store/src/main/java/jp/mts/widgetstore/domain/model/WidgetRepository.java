package jp.mts.widgetstore.domain.model;

import java.util.List;

public interface WidgetRepository {

	void save(Widget widget);
	List<Widget> findByCategory(String categoryId);
	Widget findById(WidgetId widgetId);

}
