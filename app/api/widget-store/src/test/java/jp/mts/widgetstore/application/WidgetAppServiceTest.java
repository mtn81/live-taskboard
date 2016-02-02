package jp.mts.widgetstore.application;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import jp.mts.widgetstore.domain.model.Position;
import jp.mts.widgetstore.domain.model.Size;
import jp.mts.widgetstore.domain.model.Widget;
import jp.mts.widgetstore.domain.model.WidgetId;
import jp.mts.widgetstore.domain.model.WidgetRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;

import org.junit.Test;

public class WidgetAppServiceTest {

	@Tested	WidgetAppService target = new WidgetAppService();
	@Injectable WidgetRepository widgetRepository;
	
	@Test
	public void test_save() {
		
		WidgetId widgetId = new WidgetId("cate01", "w01");
		Position position = new Position(10, 20);
		Size size = new Size(30, 40);

		new Expectations() {{
			widgetRepository.save((Widget)any);
		}};
		
		target.changeGraphic(widgetId, position, size);
		
	}

	
	@Test
	public void test_findWidgetsInCategory(){
		
		List<Widget> foundWidgets = new ArrayList<>();
		
		new Expectations() {{
			widgetRepository.findByCategory("cate01");
				result = foundWidgets;
		}};
		
		List<Widget> widgets = target.findWidgetsInCategory("cate01");
		
		assertThat(widgets, is(foundWidgets));
	}

}
