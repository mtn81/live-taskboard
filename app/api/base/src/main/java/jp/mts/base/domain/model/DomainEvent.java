package jp.mts.base.domain.model;

import java.util.Date;

import jp.mts.base.aspect.ClientContext;
import jp.mts.libs.event.Event;
import jp.mts.libs.event.EventDelegateType;

public abstract class DomainEvent implements Event {
	
	private Date occurred;
	private DomainEventConfig config;
	private String publisherId;
	
	public DomainEvent(){
		publisherId = ClientContext.clientId();
		occurred = new Date();
		config = this.getClass().getAnnotation(DomainEventConfig.class);
		if(config == null) throw new IllegalArgumentException();
	}
	

	public String publisherId() {
		return publisherId;
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
