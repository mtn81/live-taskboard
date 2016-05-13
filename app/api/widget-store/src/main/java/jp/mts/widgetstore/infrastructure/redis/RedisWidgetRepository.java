package jp.mts.widgetstore.infrastructure.redis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jp.mts.base.util.MapUtils;
import jp.mts.libs.cache.RedisCacheMap;
import jp.mts.widgetstore.domain.model.Position;
import jp.mts.widgetstore.domain.model.Size;
import jp.mts.widgetstore.domain.model.Widget;
import jp.mts.widgetstore.domain.model.WidgetBuilder;
import jp.mts.widgetstore.domain.model.WidgetId;
import jp.mts.widgetstore.domain.model.WidgetRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.lambdaworks.redis.RedisClient;

@Repository
public class RedisWidgetRepository implements WidgetRepository {
	
	private RedisCacheMap<WidgetId, Widget> widgetCacheMap;
	private RedisCacheMap<String, List<String>> widgetIdCacheMap;
	
	@Autowired
	public RedisWidgetRepository(RedisClient redisClient) {
		
		this.widgetCacheMap = new RedisCacheMap<WidgetId, Widget>(
				redisClient, "social_auth_process",
				new WidgetIdKeyEncoder(),
				new WidgetValueEncoder());

		this.widgetIdCacheMap = new RedisCacheMap<String, List<String>>(
				redisClient, "social_auth_process",
				new RedisCacheMap.IdenticalEncoder<String>(),
				new WidgetIdListValueEncoder());
	}

	@Override
	public void save(Widget widget) {
		widgetCacheMap.put(widget.id(), widget);

		Optional<List<String>> widgetidsInCategory = widgetIdCacheMap.get(widget.id().categoryId());
		List<String> widgetIds = widgetidsInCategory.isPresent() ? widgetidsInCategory.get() : Lists.newArrayList();
		if (!widgetIds.contains(widget.id().widgetId())) {
			widgetIds.add(widget.id().widgetId());
		}
		widgetIdCacheMap.put(widget.id().categoryId(), widgetIds);
	}

	@Override
	public List<Widget> findByCategory(String categoryId) {
		Optional<List<String>> found = widgetIdCacheMap.get(categoryId);
		if(!found.isPresent()) {
			return new ArrayList<>();
		}

		return widgetCacheMap.get(found.get().stream()
				.map(wid -> new WidgetId(categoryId, wid))
				.collect(Collectors.toList()));
	}

	@Override
	public Optional<Widget> findById(WidgetId widgetId) {
		return widgetCacheMap.get(widgetId);
	}
	
	private static class WidgetIdListValueEncoder implements RedisCacheMap.Encoder<Map<String, String>, List<String>>{
		@Override
		public Map<String, String> encode(List<String> value) {
			return MapUtils.pairs("widget_ids", StringUtils.join(value, ","));
		}
		@Override
		public List<String> decode(Map<String, String> value) {
			String widgetIds = (String)value.get("widget_ids");
			return Lists.newArrayList(widgetIds.split("\\,"));
		}
	}
	private static class WidgetIdKeyEncoder implements RedisCacheMap.Encoder<String, WidgetId>{
		@Override
		public WidgetId decode(String value) {
			String[] parts = value.split("@");
			return new WidgetId(parts[1], parts[0]);
		}
		@Override
		public String encode(WidgetId value) {
			return value.widgetId() + "@" + value.categoryId();
		}
	}
	private static class WidgetValueEncoder implements RedisCacheMap.Encoder<Map<String, String>, Widget>{
		@Override
		public Widget decode(Map<String, String> value) {
			return new WidgetBuilder(
					new Widget(
						new WidgetId(
							(String)value.get("category_id"), 
							(String)value.get("widget_id"))))
				.size(new Size(
						Integer.parseInt(value.get("width")), 
						Integer.parseInt(value.get("height"))))
				.position(new Position(
						Integer.parseInt(value.get("x")), 
						Integer.parseInt(value.get("y"))))
				.get();
		}
		@Override
		public Map<String, String> encode(Widget widget) {
			return MapUtils.pairs(
				"category_id", widget.widgetId().categoryId(),
				"widget_id", widget.widgetId().widgetId(),
				"x", String.valueOf(widget.position().x()),
				"y", String.valueOf(widget.position().y()),
				"width", String.valueOf(widget.size().width()),
				"height", String.valueOf(widget.size().height()));
		}
	}

}
