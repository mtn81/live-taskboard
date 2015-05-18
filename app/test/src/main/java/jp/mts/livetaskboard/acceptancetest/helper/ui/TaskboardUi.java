package jp.mts.livetaskboard.acceptancetest.helper.ui;

import static jp.mts.livetaskboard.acceptancetest.helper.ui.UiContext.pageOf;
import jp.mts.livetaskboard.acceptancetest.helper.ui.page.TaskboardPage;

public class TaskboardUi {
	private TaskboardPage page = pageOf(TaskboardPage.class);

	public boolean displaysUserName(String userName){ 
		return userName.equals(page.loginUserName()); 
	}
}
