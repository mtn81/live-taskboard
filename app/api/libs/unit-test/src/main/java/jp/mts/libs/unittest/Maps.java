package jp.mts.libs.unittest;

import java.util.HashMap;
import java.util.Map;

public class Maps {
	
	private Map<String, Object> value = new HashMap<>(); 
	
	public static Map<String, Object> map(String key, Object value) {
		return new Maps().e(key, value).get();
	}
	public Maps e(String key, Object value){
		this.value.put(key, value);
		return this;
	}
	
	public Map<String, Object> get() {
		return value;
	}
}
