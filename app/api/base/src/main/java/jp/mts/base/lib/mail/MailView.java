package jp.mts.base.lib.mail;

public abstract class MailView {

	private static String siteBaseUrl;

	public static void setSiteBaseUrl(String siteBaseUrl) {
		MailView.siteBaseUrl = siteBaseUrl;
	}
	
	public String getSiteBaseUrl() {
		return siteBaseUrl;
	}
	
}
