package jp.mts.libs.event.eventstore;

import java.util.Date;

public class StoredEvent {

	private long eventId;
	private Date occurred;
	private String eventType;
	private byte[] eventBody;
	
	public StoredEvent(long eventId, Date occurred, String eventType, byte[] eventBody) {
		this.eventId = eventId;
		this.occurred = occurred;
		this.eventType = eventType;
		this.eventBody = eventBody;
	}
	public StoredEvent(Date occurred, String eventType, byte[] eventBody) {
		this.occurred = occurred;
		this.eventType = eventType;
		this.eventBody = eventBody;
	}

	public long getEventId() {
		return eventId;
	}

	public Date getOccurred() {
		return occurred;
	}

	public String getEventType() {
		return eventType;
	}

	public byte[] getEventBody() {
		return eventBody;
	}
}
