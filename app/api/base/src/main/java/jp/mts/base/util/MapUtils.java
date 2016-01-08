package jp.mts.base.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class MapUtils {
	
	public static <K, V> Map<K, V> pairs(Object... keyAndValues) {
		if(keyAndValues == null || keyAndValues.length == 0) {
			return new HashMap<K,V>();
		}
		if(keyAndValues.length % 2 != 0){
			throw new IllegalArgumentException("argment length mast be even");
		}
		
		Map<K, V> map = new LinkedHashMap<>();
		K key = null;
		for (int i = 0; i < keyAndValues.length; i++) {
			if (i % 2 == 0) {
				key = (K)keyAndValues[i];
			} else {
				map.put(key, (V)keyAndValues[i]);
			}
		}
		
		return map;
	}
	
	public static <K1, V1, K2, V2> Map<K1, V1> map(
			Map<K2, V2> src, 
			Function<K2, K1> keyMapper,
			Function<V2, V1> valueMapper) {
		
		Map<K1, V1> map = new HashMap<>();
		src.forEach((key, value) -> map.put(keyMapper.apply(key), valueMapper.apply(value)));
		return map;
	}
}
