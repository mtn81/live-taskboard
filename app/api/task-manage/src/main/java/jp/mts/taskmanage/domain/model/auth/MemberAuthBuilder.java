package jp.mts.taskmanage.domain.model.auth;

import java.util.Date;

import jp.mts.taskmanage.domain.model.member.MemberId;

public class MemberAuthBuilder {
	
	private MemberAuth target;

	public MemberAuthBuilder(MemberAuth target) {
		this.target = target;
	}
	
	public MemberAuthBuilder setMemberId(MemberId memberId) {
		target.setMemberId(memberId);
		return this;
	}
	public MemberAuthBuilder setExpireTime(Date expireTime) {
		target.setExpireTime(expireTime);
		return this;
	}
	
	public MemberAuth get() {
		return target;
	}
	
}
