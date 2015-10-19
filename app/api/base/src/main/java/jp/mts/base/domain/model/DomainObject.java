package jp.mts.base.domain.model;


public abstract class DomainObject {
	protected static DomainEventPublisher domainEventPublisher;
	protected static DomainCalendar calendar = new DomainCalendar();
	
	public static void setDomainEventPublisher(DomainEventPublisher domainEventPublisher) {
		DomainObject.domainEventPublisher = domainEventPublisher;
	}
	public static void setDomainCalendar(DomainCalendar calendar) {
		DomainObject.calendar = calendar;
	}
	
	
}
