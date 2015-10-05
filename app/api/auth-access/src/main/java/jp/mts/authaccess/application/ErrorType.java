package jp.mts.authaccess.application;

public enum ErrorType implements jp.mts.base.application.ErrorType {
	AUTH_FAILED("e001", "認証に失敗しました。IDまたはパスワードが間違っています。"),
	USER_ID_ALREADY_EXISTS("e002", "指定されたユーザIDは利用できません。"),
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
