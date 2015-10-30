package jp.mts.taskmanage.infrastructure.cooperation;

import static org.hamcrest.CoreMatchers.nullValue;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class AuthApi {

	private static Logger logger = LoggerFactory.getLogger(AuthApi.class);
	private String baseUrl;
	
	public AuthApi(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public JSONObject loadAuth(String authId) {
		try {
			HttpResponse<JsonNode> response = Unirest.get(baseUrl + "/auth/{authId}")
				.routeParam("authId", authId)
				.asJson();
			
			if (response.getStatus() != 200) {
				logger.debug("auth access rest api error", response.getBody().toString());
				return null;
			}
			
			return response.getBody().getObject();

		} catch (UnirestException e) {
			logger.warn("failed to call auth access rest api", e);
			return null;
		}
	}

}
