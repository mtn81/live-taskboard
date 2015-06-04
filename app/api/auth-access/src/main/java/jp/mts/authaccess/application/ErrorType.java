package jp.mts.authaccess.application;

public enum ErrorType {
	AUTH_FAILED("e001", "認証に失敗しました。IDまたはパスワードが間違っています。")
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
