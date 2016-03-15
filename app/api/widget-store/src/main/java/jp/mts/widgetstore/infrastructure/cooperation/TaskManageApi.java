package jp.mts.widgetstore.infrastructure.cooperation;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class TaskManageApi {

	private String baseUri;
	
	public TaskManageApi(String baseUri) {
		this.baseUri = baseUri;
	}

	public String loadGroup(String authId, String groupId) {
		try {
			HttpResponse<JsonNode> response  = Unirest.get(this.baseUri + "/groups/" + groupId)
				.header("X-AuthAccess-AuthId", authId)
				.asJson();
			if (response.getStatus() != 200) {
				return null;
			}
			return response.getBody().getObject().getJSONObject("data").getString("groupId");
		} catch (UnirestException e) {
			return null;
		}
	}
}
