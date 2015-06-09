package jp.mts.livetaskboard.acceptancetest.helper.ui.page;

import java.util.List;
import java.util.stream.Collectors;

import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.WebDriver;

public class LoginPage extends TestablePage {
	
	public LoginPage(WebDriver webDriver){
		super(webDriver);
	}

	@Override
	protected String urlHash() {
		return "login";
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

	public List<String> errorMessages() {
		FluentWebElement msgs = findElement("div.global-msgs");
		return msgs.find("li").stream()
				.map(element -> element.getText())
				.collect(Collectors.toList());
	}
}
