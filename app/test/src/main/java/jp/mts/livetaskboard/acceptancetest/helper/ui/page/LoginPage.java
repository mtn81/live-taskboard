package jp.mts.livetaskboard.acceptancetest.helper.ui.page;

import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.WebDriver;

public class LoginPage extends TestablePage {
	
	public LoginPage(WebDriver webDriver){
		super(webDriver);
	}

	@Override
	public String getUrl() {
		return hashedUrl("login");
	}
	
	public void login(String loginId, String password){
		await("form#loginForm");
		loginIdInput().fill().with(loginId);
		passwordInput().fill().with(password);
		loginAction().click();
	}
	
	private FluentWebElement loginIdInput(){
		return findElement("input#loginForm_loginId");
	}
	private FluentWebElement passwordInput(){
		return findElement("input#loginForm_password");
	}
	private FluentWebElement loginAction(){
		return findElement("button#loginForm_loginAction");
	}
}
