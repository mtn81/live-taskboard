package jp.mts.authaccess.domain.model;

public class UserFixture {
	private User user;
	
	public UserFixture(){
		this("u01");
	}
	public UserFixture(String userId){
		user = new User(new UserId(userId), "task@test.jp", "pass", "タスク太郎");
	}
	public UserFixture setEmail(String email) {
		user.setEmail(email);
		return this;
	}
	public UserFixture setEncryptedPassword(String encryptedPassword) {
		user.setEncryptedPassword(encryptedPassword);
		return this;
	}
	public UserFixture setName(String name) {
		user.setName(name);
		return this;
	}
	public UserFixture setUserActivation(UserActivation activation) {
		user.setUserActivation(activation);
		return this;
	}
	public UserFixture setStatus(UserStatus status) {
		user.setStatus(status);
		return this;
	}
	
	public User get(){
		return user;
	}
}
