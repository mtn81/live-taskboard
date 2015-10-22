package jp.mts.authaccess.domain.model;

public class UserFixture {
	private User user;
	
	public UserFixture(){
		this("u01");
	}
	public UserFixture(String userId){
		user = new User(new UserId(userId),"task@test.jp", "pass", "タスク太郎");
	}
	public UserFixture setUserActivation(UserActivation activation) {
		user.setUserActivation(activation);
		return this;
	}
	
	public User get(){
		return user;
	}
}
