package jp.mts.widgetstore.domain.model;

public class WidgetId {
	
	private String categoryId;
	private String widgetId;

	public WidgetId(String categoryId, String widgetId) {
		this.categoryId = categoryId;
		this.widgetId = widgetId;
	}

	public String categoryId() {
		return categoryId;
	}

	public String widgetId() {
		return widgetId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((categoryId == null) ? 0 : categoryId.hashCode());
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
		WidgetId other = (WidgetId) obj;
		if (categoryId == null) {
			if (other.categoryId != null)
				return false;
		} else if (!categoryId.equals(other.categoryId))
			return false;
		if (widgetId == null) {
			if (other.widgetId != null)
				return false;
		} else if (!widgetId.equals(other.widgetId))
			return false;
		return true;
	}
	
	
	
}
