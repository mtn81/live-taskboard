package jp.mts.authaccess.rest;

import jp.mts.authaccess.rest.presentation.model.StatusGet;
import jp.mts.base.rest.RestResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StatusApi {
	
	@RequestMapping(
		method = RequestMethod.GET,
		value = "/status")
	public RestResponse<StatusGet> collectStatus() {
		return RestResponse.of(new StatusGet());
	}
}
