package jp.mts.widgetstore.domain.model;

import java.util.List;
import java.util.Optional;

public interface WidgetRepository {

	void save(Widget widget);
	List<Widget> findByCategory(String categoryId);
	Optional<Widget> findById(WidgetId widgetId);

}
