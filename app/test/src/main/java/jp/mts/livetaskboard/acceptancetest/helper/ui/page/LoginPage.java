package jp.mts.livetaskboard.acceptancetest.helper.ui.page;

import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.WebDriver;

public class LoginPage extends FluentPage {
	
	public LoginPage(WebDriver webDriver){
		super(webDriver);
	}

	@Override
	public String getUrl() {
		return "http://192.168.77.11:9000/";
	}
	
	public void login(String loginId, String password){
		await().atMost(5000).until("form#loginForm").isPresent();
		loginIdInput().fill().with(loginId);
		passwordInput().fill().with(password);
		loginAction().click();
	}
	
	private FluentWebElement loginIdInput(){
		return findFirst("input#loginForm_loginId");
	}
	private FluentWebElement passwordInput(){
		return findFirst("input#loginForm_password");
	}
	private FluentWebElement loginAction(){
		return findFirst("button#loginForm_loginAction");
	}
}
