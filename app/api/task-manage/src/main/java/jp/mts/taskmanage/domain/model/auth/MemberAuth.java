package jp.mts.taskmanage.domain.model.auth;

import jp.mts.taskmanage.domain.model.MemberId;

public class MemberAuth {

	private MemberId memberId;
	private boolean expired;

	public MemberAuth(MemberId memberId, boolean expired) {
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
