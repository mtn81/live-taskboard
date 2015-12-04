package jp.mts.widgetstore.infrastructure.jdbc.repository;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Repository;

import jp.mts.widgetstore.domain.model.Position;
import jp.mts.widgetstore.domain.model.Size;
import jp.mts.widgetstore.domain.model.Widget;
import jp.mts.widgetstore.domain.model.WidgetBuilder;
import jp.mts.widgetstore.domain.model.WidgetId;
import jp.mts.widgetstore.domain.model.WidgetRepository;
import jp.mts.widgetstore.infrastructure.jdbc.model.WidgetModel;

@Repository
public class JdbcWidgetRepository implements WidgetRepository {

	@Override
	public void save(Widget widget) {
		WidgetModel model = _findById(widget.widgetId());
		if (model == null) {
			model = new WidgetModel();
		}
		
		model.set(
				"category_id", widget.widgetId().categoryId(),
				"widget_id", widget.widgetId().widgetId(),
				"x", widget.position().x(),
				"y", widget.position().y(),
				"width", widget.size().width(),
				"height", widget.size().height())
			.saveIt();
	}

	@Override
	public List<Widget> findByCategory(String categoryId) {
		return WidgetModel.find("category_id=?", categoryId)
				.stream().map(w -> toDomain((WidgetModel)w))
				.collect(toList());
	}
	@Override
	public Widget findById(WidgetId widgetId) {
		return toDomain(_findById(widgetId));
	}
	
	private WidgetModel _findById(WidgetId widgetId) {
		return WidgetModel.findFirst("category_id=? and widget_id=?", 
				widgetId.categoryId(), widgetId.widgetId());
	}

	private Widget toDomain(WidgetModel model) {
		if(model == null) return null;

		return new WidgetBuilder(
				new Widget(
					new WidgetId(
						model.getString("category_id"), 
						model.getString("widget_id"))))
			.size(new Size(
					model.getInteger("width"), 
					model.getInteger("height")))
			.position(new Position(
					model.getInteger("x"), 
					model.getInteger("y")))
			.get();
	}


}
