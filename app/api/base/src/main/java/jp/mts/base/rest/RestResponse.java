package jp.mts.base.rest;

import java.util.ArrayList;
import java.util.List;

import jp.mts.base.application.ErrorType;

public class RestResponse<T> {
	
	private T data;
	private List<ApiError> errors = new ArrayList<>();
	
	private RestResponse(){
	}
	private RestResponse(T data){
		this.data = data;
	}
	public static <T> RestResponse<T> of(T data) {
		RestResponse<T> response = new RestResponse<>(data);
		return response;
	}
	public static <T> RestResponse<T> of(ApiError e) {
		RestResponse<T> response = new RestResponse<>();
		response.addError(e);
		return response;
	}
	
	public void addError(ApiError e){
		errors.add(e);
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

		public ApiError(ErrorType errorType) {
			this(errorType.getErrorCode(), errorType.getMessage());
		}
		public ApiError(String errorCode, String message) {
			this.errorCode = errorCode;
			this.message = message;
		}

		public String getErrorCode() {
			return errorCode;
		}
		public String getMessage() {
			return message;
		}
	}

}
