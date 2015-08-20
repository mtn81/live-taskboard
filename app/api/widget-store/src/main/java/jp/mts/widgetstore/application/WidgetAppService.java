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
public class WidgetAppService {
	
	@Autowired
	private WidgetRepository widgetRepository;

	@Transactional
	public void save(WidgetId widgetId, Position position, Size size) {
		
		widgetRepository.save(
				new Widget(widgetId, position, size));
	}

	public List<Widget> findWidgetsInCategory(String categoryId) {
		return widgetRepository.findByCategory(categoryId);
	}

}
