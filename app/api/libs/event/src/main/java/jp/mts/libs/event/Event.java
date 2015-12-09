package jp.mts.libs.event;

import java.util.Date;

public interface Event {
	String publisherId();
	Date occurred();
	String eventType();
	EventDelegateType eventDelegateType();
}
