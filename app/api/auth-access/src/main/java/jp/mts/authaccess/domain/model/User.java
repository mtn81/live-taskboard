package jp.mts.authaccess.domain.model;

public class User {
	
	private UserId id;
	private String email;
	private String password;
	private String name;
	
	public User(UserId id) {
		this.id = id;
	}
	
	public UserId id() {
		return this.id;
	}
	public String email() {
		return email;
	}
	public String name() {
		return name;
	}
	public String password() {
		return password;
	}

	void setEmail(String email) {
		this.email = email;
	}

	void setName(String name) {
		this.name = name;
	}

	void setPassword(String password) {
		this.password = password;
	}

}
