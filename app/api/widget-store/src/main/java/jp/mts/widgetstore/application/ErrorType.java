package jp.mts.widgetstore.application;

public enum ErrorType implements jp.mts.base.application.ErrorType {
	NOT_AUTHORIZED("e-ws-001", "操作が許可されていません");
	;
	
	private String errorCode;
	private String message;
	
	private ErrorType(String errorCode, String message) {
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
