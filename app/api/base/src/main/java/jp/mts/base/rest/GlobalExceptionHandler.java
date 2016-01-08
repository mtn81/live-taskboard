package jp.mts.base.rest;

import jp.mts.base.application.ApplicationException;
import jp.mts.base.rest.RestResponse.ApiError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ApplicationException.class)
	@ResponseBody
	public RestResponse<?> handleApplicationException(ApplicationException e) {
		if (e.getCause() != null) {
			logger.debug("application exception caused by", e.getCause());
		}
		return RestResponse.of(new ApiError(e.getErrorType()));
	}

}
