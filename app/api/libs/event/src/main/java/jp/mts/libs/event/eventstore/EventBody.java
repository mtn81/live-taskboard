package jp.mts.libs.event.eventstore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBody {
	private Map<String, Object> value = new HashMap<>();

	public EventBody(Map<String, Object> value) {
		this.value = value;
	} 
	
	public String asString(String keyPath) {
		List<String> keys = Arrays.asList(keyPath.split("\\."));
		EventBody target = this;
		for(String key : keys){
			if(keys.indexOf(key) == keys.size() - 1){
				return String.valueOf(target.value.get(key));
			}else{
				target = new EventBody((Map)value.get(key));
			}
		};
		return null;
	}
	
	public <T> T as(Class<T> clazz, String keyPath) {
		
		List<String> keys = Arrays.asList(keyPath.split("\\."));
		EventBody target = this;
		for(String key : keys){
			if(keys.indexOf(key) == keys.size() - 1){
				return (T)target.value.get(key);
			}else{
				target = new EventBody((Map)value.get(key));
			}
		};
		return null;
	}

}
