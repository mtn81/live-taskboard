package jp.mts.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.javalite.common.Convert;

public class ListUtils {
	
	public static <K, T> Map<K, List<T>> group(List<T> src, Function<T, K> keyConverter){
		Map<K, List<T>> map = new HashMap<>();
		src.forEach(e -> {
			K keyValue = keyConverter.apply(e);
			if(!map.containsKey(keyValue)) 
				map.put(keyValue, new ArrayList<>());
			map.get(keyValue).add(e);
		});
		return map;
	}
	public static <S, T> List<T> convert(List<S> source, Function<S, T> mapper) {
		return source.stream()
				.map(mapper).collect(Collectors.toList());
	}
}
