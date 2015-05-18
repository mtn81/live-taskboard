package jp.mts.livetaskboard.acceptancetest.helper.ui.page;

import org.openqa.selenium.WebDriver;

public class TaskboardPage extends TestablePage {
	
	public TaskboardPage(WebDriver webDriver){
		super(webDriver);
	}

	@Override
	public String getUrl() {
		return hashedUrl("taskboard");
	}
	
	public String loginUserName(){
		return findElement("span.login-user").getText();
	}
}
