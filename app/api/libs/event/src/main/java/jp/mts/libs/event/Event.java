package jp.mts.libs.event;

import java.util.Date;

public interface Event {
	Date occurred();
	String eventType();
	EventDelegateType eventDelegateType();
}
