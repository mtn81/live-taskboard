package jp.mts.livetaskboard.acceptancetest.helper.api;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class AuthApi {
	
	public void registerUser(String authId, String password, String userName){
		Client client = Client.create();
		WebResource webResource = client.resource("http://localhost:8080/auth-access/api/users/");
		webResource.type(MediaType.APPLICATION_JSON).post(
				new FlatStringJson()
					.e("authId", authId)
					.e("password", password)
					.e("userName", userName)
					.jsonString());
	}
	
	private static class FlatStringJson {
		private Map<String, String> entries = new LinkedHashMap<>();
		
		public FlatStringJson e(String key, String value){
			this.entries.put(key, value);
			return this;
		}
		
		public String jsonString(){
			StringBuilder sb = new StringBuilder("{");
			for(Entry<String, String> e : entries.entrySet()) {
				sb.append("\"" + e.getKey() + "\":\"" + e.getValue() + "\",");
			}
			sb.replace(sb.length() - 1, sb.length(), "");
			sb.append("}");
			return sb.toString();
		}
	}

}
