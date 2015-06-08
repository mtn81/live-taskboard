package jp.mts.authaccess.domain.model;

public class AuthFixture {
	public Auth build(){
		return new Auth(new UserId("taro"), "タスク太郎");
	}
}
