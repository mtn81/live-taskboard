package jp.mts.taskmanage.domain.model;

public class MemberAuth {

	private MemberId memberId;
	private boolean expired;

	public MemberAuth(MemberId memberId, boolean expired) {
		super();
		this.memberId = memberId;
		this.expired = expired;
	}
	
	public MemberId memberId() {
		return memberId;
	}
	public boolean expired() {
		return expired;
	}
	
}
