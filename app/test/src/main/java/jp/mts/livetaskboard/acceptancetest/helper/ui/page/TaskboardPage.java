package jp.mts.livetaskboard.acceptancetest.helper.ui.page;

import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;

public class TaskboardPage extends FluentPage {
	
	public TaskboardPage(WebDriver webDriver){
		super(webDriver);
	}

	@Override
	public String getUrl() {
		return "http://192.168.77.11:9000/#/taskboard";
	}
	
	public String loginUserName(){
		await().until("span.login-user").isPresent();
		return findFirst("span.login-user").getText();
	}
}
