package jp.mts.livetaskboard.acceptancetest.helper.ui;

import static jp.mts.livetaskboard.acceptancetest.helper.ui.UiContext.pageOf;
import jp.mts.livetaskboard.acceptancetest.helper.ui.page.LoginPage;

public class LoginUi {
	private LoginPage page = pageOf(LoginPage.class);
	
	public void login(String loginId, String password){
		page.go();
		page.login(loginId, password);
	}
	
	public boolean promptLogin() {
		return page.isAppearing();
	}

}
