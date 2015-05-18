package jp.mts.authaccess.domain.model;

public class Auth {
	
	private String id;
	private String name;

	public Auth(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String id(){
		return id;
	}
	public String name(){
		return name;
	}
	
}
