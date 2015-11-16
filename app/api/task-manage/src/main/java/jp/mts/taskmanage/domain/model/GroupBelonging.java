package jp.mts.taskmanage.domain.model;

import jp.mts.base.domain.model.CompositeId;
import jp.mts.base.domain.model.DomainEntity;

public class GroupBelonging extends DomainEntity<CompositeId>{

	private GroupId groupId;
	private MemberId memberId;
	private boolean isAdmin;

	public GroupBelonging(
			GroupId groupId, 
			MemberId memberId,
			boolean isAdmin) {
		super(CompositeId.of(groupId, memberId));
		this.groupId = groupId;
		this.memberId = memberId;
		this.isAdmin = isAdmin;
	}
	
	public GroupId groupId(){
		return groupId;
	}
	public MemberId memberId(){
		return memberId;
	}
	public boolean isAdmin() {
		return isAdmin;
	}

	void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
}
