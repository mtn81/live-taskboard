package jp.mts.base.rest;

import jp.mts.base.application.ApplicationException;
import jp.mts.base.rest.RestResponse.ApiError;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ApplicationException.class)
	@ResponseBody
	public RestResponse<?> handleApplicationException(ApplicationException e) {
		return RestResponse.of(new ApiError(e.getErrorType()));
	}
}
