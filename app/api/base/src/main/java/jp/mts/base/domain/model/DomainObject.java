package jp.mts.base.domain.model;


public abstract class DomainObject {
	protected static DomainEventPublisher domainEventPublisher;
	
	public static void setDomainEventPublisher(DomainEventPublisher domainEventPublisher) {
		DomainObject.domainEventPublisher = domainEventPublisher;
	}
}
