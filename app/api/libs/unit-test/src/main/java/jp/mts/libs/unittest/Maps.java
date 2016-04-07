package jp.mts.libs.unittest;

import java.util.HashMap;
import java.util.Map;

public class Maps {
	
	private Map<String, Object> value = new HashMap<>(); 
	
	public static <T> Map<String, T> map(String key, Object value) {
		return (Map<String, T>)new Maps().e(key, value).get();
	}
	public Maps e(String key, Object value){
		this.value.put(key, value);
		return this;
	}
	
	public <T> Map<String, T> get() {
		return (Map<String, T>)value;
	}
}
