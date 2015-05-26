package jp.mts.authaccess.domain.model;

public class UserBuilder {
	private User user;
	
	public UserBuilder(){
		user = new User(new UserId("u01"));
		user.setEmail("task@test.jp");
		user.setName("タスク太郎");
		user.setPassword("pass");
	}
	
	public User build(){
		return user;
	}
}
