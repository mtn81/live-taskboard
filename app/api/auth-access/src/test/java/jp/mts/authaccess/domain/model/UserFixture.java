package jp.mts.authaccess.domain.model;

public class UserFixture {
	private User user;
	
	public UserFixture(){
		user = new User(new UserId("u01"),"task@test.jp", "pass", "タスク太郎");
	}
	
	public User build(){
		return user;
	}
}
