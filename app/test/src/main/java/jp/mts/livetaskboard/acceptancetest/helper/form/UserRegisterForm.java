package jp.mts.livetaskboard.acceptancetest.helper.form;

public class UserRegisterForm {
	private String userId = "taro";
	private String email = "taro@test.jp";
	private String password = "pass";
	private String userName = "タスク太郎";

	public String userId() {
		return userId;
	}
	public UserRegisterForm userId(String userId) {
		this.userId = userId;
		return this;
	}
	public String email() {
		return email;
	}
	public UserRegisterForm email(String email) {
		this.email = email;
		return this;
	}
	public String password() {
		return password;
	}
	public UserRegisterForm password(String password) {
		this.password = password;
		return this;
	}
	public String userName() {
		return userName;
	}
	public UserRegisterForm userName(String userName) {
		this.userName = userName;
		return this;
	}
}
