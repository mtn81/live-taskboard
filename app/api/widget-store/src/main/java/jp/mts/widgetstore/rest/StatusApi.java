package jp.mts.widgetstore.rest;

import jp.mts.base.rest.RestResponse;
import jp.mts.widgetstore.rest.presentation.model.StatusGet;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StatusApi {
	
	@RequestMapping(
		method=RequestMethod.GET,
		value="/status")
	public RestResponse<StatusGet> collectStatus() {
		return RestResponse.of(new StatusGet());
	}
}
