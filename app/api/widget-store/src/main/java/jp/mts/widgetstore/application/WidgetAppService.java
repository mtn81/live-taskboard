package jp.mts.widgetstore.application;

import java.util.List;

import jp.mts.widgetstore.domain.model.Position;
import jp.mts.widgetstore.domain.model.Size;
import jp.mts.widgetstore.domain.model.Widget;
import jp.mts.widgetstore.domain.model.WidgetId;
import jp.mts.widgetstore.domain.model.WidgetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WidgetAppService {
	
	@Autowired
	private WidgetRepository widgetRepository;

	public void changeGraphic(WidgetId widgetId, Position position, Size size) {
		Widget widget = widgetRepository.findById(widgetId);
		if (widget == null) {
			widget = new Widget(widgetId);
		}
		
		widget.change(position, size);
		
		widgetRepository.save(widget);
	}

	public List<Widget> findWidgetsInCategory(String categoryId) {
		return widgetRepository.findByCategory(categoryId);
	}

}
