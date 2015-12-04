package jp.mts.base.domain.model;

import java.util.Date;

import jp.mts.libs.event.Event;
import jp.mts.libs.event.EventDelegateType;

public abstract class DomainEvent implements Event {
	
	private Date occurred;
	private DomainEventConfig config;
	
	public DomainEvent(){
		occurred = new Date();
		config = this.getClass().getAnnotation(DomainEventConfig.class);
		if(config == null) throw new IllegalArgumentException();
	}
	
	public Date occurred(){
		return occurred;
	}
	
	public String eventType(){
		return config.eventType();
	}
	public EventDelegateType eventDelegateType() {
		return config.delegateType();
	}

}
