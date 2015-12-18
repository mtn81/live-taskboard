package jp.mts.authaccess.application;

public enum ErrorType implements jp.mts.base.application.ErrorType {
	AUTH_FAILED("e001", "認証に失敗しました。IDまたはパスワードが間違っています。"),
	ACTIVATION_NOT_FOUND("e002", "有効化できません"),
	ACTIVATION_EXPIRED("e003", "ユーザを有効にできる期間を過ぎています。再度ユーザを登録してください。"),
	USER_ID_ALREADY_EXISTS("e004", "指定されたユーザIDは利用できません。"),
	AUTH_NOT_FOUND("e005", "認証が必要です。"),
	SOCIAL_AUTH_FAILED("e006", "認証に失敗しました。"),
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
