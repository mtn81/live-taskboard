package jp.mts.authaccess.domain.model.proper;

import jp.mts.authaccess.domain.model.proper.ProperUser;
import jp.mts.authaccess.domain.model.proper.ProperUserActivation;
import jp.mts.authaccess.domain.model.proper.ProperUserId;
import jp.mts.authaccess.domain.model.proper.ProperUserStatus;

public class ProperUserFixture {
	private ProperUser user;
	
	public ProperUserFixture(){
		this("u01");
	}
	public ProperUserFixture(String userId){
		user = new ProperUser(new ProperUserId(userId), "task@test.jp", "pass", "タスク太郎");
	}
	public ProperUserFixture setEmail(String email) {
		user.setEmail(email);
		return this;
	}
	public ProperUserFixture setEncryptedPassword(String encryptedPassword) {
		user.setEncryptedPassword(encryptedPassword);
		return this;
	}
	public ProperUserFixture setName(String name) {
		user.setName(name);
		return this;
	}
	public ProperUserFixture setUserActivation(ProperUserActivation activation) {
		user.setUserActivation(activation);
		return this;
	}
	public ProperUserFixture setStatus(ProperUserStatus status) {
		user.setStatus(status);
		return this;
	}
	public ProperUserFixture setUseEmailNotification(boolean useEmailNotification) {
		user.setUseEmailNotification(useEmailNotification);
		return this;
	}
	
	public ProperUser get(){
		return user;
	}
}
