package jp.mts.base.domain.model;

import java.util.Date;

import jp.mts.libs.event.Event;

public abstract class DomainEvent implements Event {
	
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
