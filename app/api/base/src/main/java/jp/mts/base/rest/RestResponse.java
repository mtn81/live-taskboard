package jp.mts.base.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jp.mts.base.application.ErrorType;

import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.collect.Lists;

public class RestResponse<T> {
	
	private T data;
	private List<ApiError> errors = new ArrayList<>();
	
	private RestResponse(){
	}
	private RestResponse(T data){
		this.data = data;
	}
	public static RestResponse<Void> empty() {
		RestResponse<Void> response = new RestResponse<>(null);
		return response;
	}
	public static <T> RestResponse<T> of(T data) {
		RestResponse<T> response = new RestResponse<>(data);
		return response;
	}
	public static <T> RestResponse<T> of(ApiError e, int statusCode) {
		setResponseStatus(statusCode);
		return of(e);
	}
	public static <T> RestResponse<T> of(ApiError e) {
		RestResponse<T> response = new RestResponse<>();
		response.addError(e);
		return response;
	}
	public static <T> RestResponse<T> of(List<ApiError> e, int statusCode) {
		setResponseStatus(statusCode);
		return of(e);
	}
	public static <T> RestResponse<T> of(List<ApiError> e) {
		RestResponse<T> response = new RestResponse<>();
		response.addErrors(e);
		return response;
	}
	public static <T> RestResponse<T> of(BindingResult bindingResult) {
		if(bindingResult.hasErrors()){
			setResponseStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return of(ApiError.errors(bindingResult));
	}
	private static void setResponseStatus(int statusCode) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		requestAttributes.getResponse().setStatus(statusCode);
	}
	
	public void addError(ApiError e){
		errors.add(e);
	}
	public void addErrors(List<ApiError> e){
		errors.addAll(e);
	}
	
	public T getData(){
		return data;
	}
	public List<ApiError> getErrors(){
		return errors;
	}
	
	public boolean hasError(String errorCode) {
		return errors.stream().anyMatch(e -> e.getErrorCode().equals(errorCode));
	}
	public static class ApiError {
		private String errorCode;
		private String message;
		private String field;

		public static List<ApiError> errors(BindingResult bindingResult) {
			List<ApiError> errors = Lists.newArrayList();
			bindingResult.getFieldErrors().forEach(fe -> {
				errors.add(new ApiError("e999", fe.getDefaultMessage(), fe.getField()));
			});
			bindingResult.getGlobalErrors().forEach(ge -> {
				errors.add(new ApiError("e999", ge.getDefaultMessage()));
			});
			return errors;
		}
		
		public ApiError(ErrorType errorType) {
			this(errorType.getErrorCode(), errorType.getMessage());
		}
		public ApiError(String errorCode, String message) {
			this(errorCode, message, null);
		}
		public ApiError(String errorCode, String message, String field) {
			this.errorCode = errorCode;
			this.message = message;
			this.field = field;
		}

		public String getErrorCode() {
			return errorCode;
		}
		public String getMessage() {
			return message;
		}
		public String getField(){
			return field;
		}
	}

}
