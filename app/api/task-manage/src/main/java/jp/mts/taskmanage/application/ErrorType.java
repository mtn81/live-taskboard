package jp.mts.taskmanage.application;

public enum ErrorType implements jp.mts.base.application.ErrorType{
	MEMBER_NOT_EXIST("e-tm-001", "指定されたメンバーは存在しません")
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
