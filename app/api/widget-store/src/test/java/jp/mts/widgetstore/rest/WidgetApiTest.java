package jp.mts.widgetstore.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import jp.mts.base.rest.RestResponse;
import jp.mts.widgetstore.application.WidgetAppService;
import jp.mts.widgetstore.domain.model.Position;
import jp.mts.widgetstore.domain.model.Size;
import jp.mts.widgetstore.domain.model.WidgetFixture;
import jp.mts.widgetstore.domain.model.WidgetId;
import jp.mts.widgetstore.rest.presentation.model.WidgetList;
import jp.mts.widgetstore.rest.presentation.model.WidgetList.WidgetView;
import jp.mts.widgetstore.rest.presentation.model.WidgetSave;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.junit.Test;

import com.google.common.collect.Lists;

public class WidgetApiTest {
	
	@Tested WidgetApi target = new WidgetApi();
	@Injectable WidgetAppService widgetAppService;

	@Test
	public void test_save() {
		
		new Expectations() {{
			widgetAppService.changeGraphic(
					new WidgetId("cate01", "w01"),
					new Position(20, 10),
					new Size(40, 30));
		}};
		
		WidgetSave widgetSave = new WidgetSave();
		widgetSave.setTop(10);
		widgetSave.setLeft(20);
		widgetSave.setHeight(30);
		widgetSave.setWidth(40);

		RestResponse<WidgetSave> response = target.save("cate01", "w01", widgetSave);
		
		assertThat(response.getData().getCategoryId(), is("cate01"));
		assertThat(response.getData().getWidgetId(), is("w01"));
	}

	@Test
	public void test_load(){
		new Expectations() {{
			widgetAppService.findWidgetsInCategory("cate01");
			result = Lists.newArrayList(
					new WidgetFixture(new WidgetId("cate01", "w01")).get(),
					new WidgetFixture(new WidgetId("cate01", "w02")).get());
		}};
		
		RestResponse<WidgetList> response = target.load("cate01");
		List<WidgetView> widgets = response.getData().getWidgets();
		
		assertThat(widgets.size(), is(2));
		assertThat(widgets.get(0).getCategoryId(), is("cate01"));
		assertThat(widgets.get(0).getWidgetId(), is("w01"));
		assertThat(widgets.get(1).getCategoryId(), is("cate01"));
		assertThat(widgets.get(1).getWidgetId(), is("w02"));
	}
}
