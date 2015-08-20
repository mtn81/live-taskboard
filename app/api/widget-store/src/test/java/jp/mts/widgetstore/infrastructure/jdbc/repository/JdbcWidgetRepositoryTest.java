package jp.mts.widgetstore.infrastructure.jdbc.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.base.unittest.JdbcTestBase;
import jp.mts.widgetstore.domain.model.Widget;
import jp.mts.widgetstore.domain.model.WidgetFixture;
import jp.mts.widgetstore.domain.model.WidgetId;

import org.junit.Test;

public class JdbcWidgetRepositoryTest extends JdbcTestBase {


	JdbcWidgetRepository target = new JdbcWidgetRepository();
	
	@Test
	public void test_persistence() {
		WidgetId widgetId = new WidgetId("cate01", "w01");
		Widget widget = new WidgetFixture(widgetId).get();
		target.save(widget);
		
		Widget found = target.findById(widgetId);
		
		assertThat(found, is(widget));
		
	}

}
