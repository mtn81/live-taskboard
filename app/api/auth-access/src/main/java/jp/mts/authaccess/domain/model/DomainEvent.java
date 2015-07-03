package jp.mts.authaccess.domain.model;

import java.util.Date;

public abstract class DomainEvent {
	
	private Date occurred;
	
	public DomainEvent(){
		occurred = new Date();
	}
	
	public Date occurred(){
		return occurred;
	}
	
	public String eventType(){
		return this.getClass().getName();
	}

}
