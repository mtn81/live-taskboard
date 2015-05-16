package jp.mts.livetaskboard.acceptancetest.helper.ui;

import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public final class UiContext {
	
	private static WebDriver webDriver = new ChromeDriver();
	
	public WebDriver webDriver() {
		return webDriver;
	}
	
	public static <T extends FluentPage> T pageOf(Class<T> pageClass){
		try {
			return pageClass.getConstructor(WebDriver.class).newInstance(webDriver);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
