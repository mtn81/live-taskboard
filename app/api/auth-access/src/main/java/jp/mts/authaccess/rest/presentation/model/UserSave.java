package jp.mts.authaccess.rest.presentation.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import jp.mts.authaccess.application.UserAppService;
import jp.mts.authaccess.domain.model.User;

public class UserSave {

	//input
	@NotBlank
	private String userId;
	@NotBlank
	@Length(max=10)
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
	private User user;
	
	public String getUserId() {
		return user.id().value();
	}
	
	//process
	public void create(UserAppService userAppService) {
		
		user = userAppService.register(
				userId,
				email, 
				userName, 
				password);
	}

	public void validateForRegister(UserAppService userAppService) {
		userAppService.validateForRegister(userId);
	}
	
}
