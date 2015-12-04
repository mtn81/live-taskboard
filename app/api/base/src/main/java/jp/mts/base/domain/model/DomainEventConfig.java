package jp.mts.base.domain.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jp.mts.libs.event.EventDelegateType;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DomainEventConfig {

	String eventType();
	EventDelegateType delegateType() default EventDelegateType.EVENT_STORE;
}
