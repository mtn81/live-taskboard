package jp.mts.base.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class ListUtils {
	
	public static <K, T> Map<K, List<T>> group(Collection<T> src, Function<T, K> keyConverter){
		Map<K, List<T>> map = new HashMap<>();
		src.forEach(e -> {
			K keyValue = keyConverter.apply(e);
			if(!map.containsKey(keyValue)) 
				map.put(keyValue, new ArrayList<>());
			map.get(keyValue).add(e);
		});
		return map;
	}
	public static <S, T> List<T> convert(Collection<S> source, Function<S, T> mapper) {
		if (source == null || source.isEmpty()) 
			return Lists.newArrayList();

		return source.stream()
				.map(mapper).collect(Collectors.toList());
	}
	public static <T> List<T> appended(List<T> src, T... append) {
		List<T> result = src == null ? Lists.newArrayList() : Lists.newArrayList(src);
		List<T> appends = append== null ? Lists.newArrayList() : Lists.newArrayList(append);
		result.addAll(appends);
		return result;
	}
}
