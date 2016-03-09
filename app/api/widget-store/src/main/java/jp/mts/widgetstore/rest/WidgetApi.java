package jp.mts.widgetstore.rest;

import jp.mts.base.rest.RestResponse;
import jp.mts.widgetstore.application.WidgetAppService;
import jp.mts.widgetstore.rest.aspect.Authorized;
import jp.mts.widgetstore.rest.presentation.model.WidgetList;
import jp.mts.widgetstore.rest.presentation.model.WidgetSave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories/{categoryId}/widgets")
public class WidgetApi {
	
	@Autowired
	private WidgetAppService widgetAppService;
	
	@RequestMapping(value="/{widgetId}", method=RequestMethod.PUT)
	public RestResponse<WidgetSave> save(
			@Authorized @PathVariable String categoryId, 
			@PathVariable String widgetId, 
			@RequestBody WidgetSave widgetSave) {

		widgetSave.save(categoryId, widgetId, widgetAppService);
		return RestResponse.of(widgetSave);
	}
	
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public RestResponse<WidgetList> load(
			@Authorized @PathVariable String categoryId) {
		
		WidgetList widgetList = new WidgetList();
		widgetList.loadByCategory(categoryId, widgetAppService);
		return RestResponse.of(widgetList);
	}

}
