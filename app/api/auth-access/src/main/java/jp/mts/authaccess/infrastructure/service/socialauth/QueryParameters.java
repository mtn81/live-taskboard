package jp.mts.authaccess.infrastructure.service.socialauth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class QueryParameters {
	private Map<String, String> params = new HashMap<>();

	public QueryParameters(String src) {
		if (src != null) {
			Arrays.stream(src.split("&")).forEach(s -> {
				String[] keyAndValue = s.split("=");
				params.put(keyAndValue[0], keyAndValue[1]);
			});
		}
	}

	public String get(String key) {
		return params.get(key);
	}

}
