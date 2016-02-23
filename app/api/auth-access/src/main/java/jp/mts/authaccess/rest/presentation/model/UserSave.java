package jp.mts.authaccess.rest.presentation.model;

import jp.mts.authaccess.application.UserAppService;
import jp.mts.authaccess.domain.model.proper.ProperUser;
import jp.mts.authaccess.domain.model.proper.ProperUserActivation;
import jp.mts.authaccess.rest.presentation.model.validator.Confirm;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Confirm(field="password", confirmField="confirmPassword")
public class UserSave {

	//required services
	private static UserAppService userAppService;
	
	public static void setUserAppService(UserAppService userAppService) {
		UserSave.userAppService = userAppService;
	}

	//input
	@NotBlank
	private String userId;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String userName;

	@NotBlank
	private String password;

	@NotBlank
	private String confirmPassword;

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setUserName(String name) {
		this.userName = name;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	//output
	private ProperUser user;
	
	public String getUserId() {
		return user.id().value();
	}
	
	//process
	public void create() {
		
		user = userAppService.register(
				userId,
				email, 
				userName, 
				password);
	}

	public void validateForRegister() {
		userAppService.validateForRegister(userId);
	}
	
}
