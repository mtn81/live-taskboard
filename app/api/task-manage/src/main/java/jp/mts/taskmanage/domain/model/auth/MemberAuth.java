package jp.mts.taskmanage.domain.model.auth;

import java.util.Date;

import jp.mts.base.domain.model.DomainObject;
import jp.mts.taskmanage.domain.model.MemberId;

import org.apache.commons.lang3.time.DateUtils;

public class MemberAuth extends DomainObject {

	private MemberId memberId;
	private Date expireTime;

	public MemberAuth(MemberId memberId) {
		this.memberId = memberId;
		this.expireTime = DateUtils.addHours(calendar.systemDate(), 1);
	}
	
	public MemberId memberId() {
		return memberId;
	}
	public Date expireTime() {
		return expireTime;
	}
	public boolean isExpired() {
		return calendar.systemDate().after(expireTime);
	}
	public MemberAuth expireExtended() {
		return new MemberAuth(memberId);
	}
	
}
