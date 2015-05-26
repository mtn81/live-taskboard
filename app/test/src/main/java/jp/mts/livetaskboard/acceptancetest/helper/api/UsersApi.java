package jp.mts.livetaskboard.acceptancetest.helper.api;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;

import jp.mts.livetaskboard.acceptancetest.helper.form.UserRegisterForm;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class UsersApi {
	
	public void registerUser(String loginId, String password){
		UserRegisterForm userRegisterForm = new UserRegisterForm()
			.userId(loginId).password(password);
		registerUser(userRegisterForm);
	}

	public void registerUser(String loginId, String password, String userName){
		UserRegisterForm userRegisterForm = new UserRegisterForm()
			.userId(loginId).password(password).userName(userName);
		registerUser(userRegisterForm);
	}

	private void registerUser(UserRegisterForm userRegisterForm){
		Client client = Client.create();
		WebResource webResource = client.resource("http://localhost:8080/auth-access/api/users/");
		webResource.type(MediaType.APPLICATION_JSON).post(
			new FlatStringJson()
				.e("userId", userRegisterForm.userId())
				.e("email", userRegisterForm.email())
				.e("name", userRegisterForm.userName())
				.e("password", userRegisterForm.password())
				.e("confirmPassword", userRegisterForm.password())
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
