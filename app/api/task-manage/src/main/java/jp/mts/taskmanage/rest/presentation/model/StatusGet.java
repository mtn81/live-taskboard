package jp.mts.taskmanage.rest.presentation.model;

public class StatusGet {

	//output
	public StatusType getApiStatus() {
		return StatusType.NORMAL;
	}
	
	public enum StatusType {
		NORMAL,
		ERROR
	}
}
