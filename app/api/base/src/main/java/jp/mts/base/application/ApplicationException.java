package jp.mts.base.application;

public class ApplicationException extends RuntimeException {
	private ErrorType errorType;

	public ApplicationException(ErrorType errorType) {
		super();
		this.errorType = errorType;
	}
	
	public boolean hasErrorOf(ErrorType errorType){
		return this.errorType == errorType;
	}
}
