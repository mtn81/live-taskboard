package jp.mts.authaccess.domain.model;

public class UserFixture {
	private User user;
	
	public UserFixture(){
		this("u01");
	}
	public UserFixture(String userId){
		user = new User(new UserId(userId),"task@test.jp", "pass", "タスク太郎");
	}
	
	public User get(){
		return user;
	}
}
