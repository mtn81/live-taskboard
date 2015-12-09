package jp.mts.libs.event.eventstore;

import java.util.Date;

public class StoredEvent {

	private long eventId;
	private Date occurred;
	private String eventType;
	private byte[] eventBody;
	private String publisherId;
	
	public StoredEvent(
			long eventId, 
			String publisherId,
			Date occurred, 
			String eventType, 
			byte[] eventBody) {
		this.eventId = eventId;
		this.publisherId = publisherId;
		this.occurred = occurred;
		this.eventType = eventType;
		this.eventBody = eventBody;
	}
	public StoredEvent(
			String publisherId,
			Date occurred, 
			String eventType, 
			byte[] eventBody) {
		
		this(0, publisherId, occurred, eventType, eventBody);
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
	public String getPublisherId() {
		return publisherId;
	}
}
