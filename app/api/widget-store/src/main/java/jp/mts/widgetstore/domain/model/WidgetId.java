package jp.mts.widgetstore.domain.model;

import java.util.Arrays;
import java.util.List;

import jp.mts.base.domain.model.DomainId;


public class WidgetId extends DomainId<List<String>> {
	
	public WidgetId(String categoryId, String widgetId) {
		super(Arrays.asList(categoryId, widgetId));
	}

	public String categoryId() {
		return value().get(0);
	}

	public String widgetId() {
		return value().get(1);
	}
}
