package jp.mts.livetaskboard.acceptancetest.helper.ui.page;

import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.WebDriver;

public abstract class TestablePage extends FluentPage {

	public TestablePage(WebDriver webDriver){
		super(webDriver);
	}
	protected FluentWebElement findElement(String cssSelector){
		await(cssSelector);
		return findFirst(cssSelector);
	}
	protected String baseUrl(){
		return "http://192.168.77.11:9000";
	}
	protected String hashedUrl(String hash){
		return baseUrl() + "/#/" + hash;
	}
	protected void await(String cssSelector) {
		await().atMost(5000).until(cssSelector).isPresent();
	}
}
