package jp.mts.taskmanage.domain.model;

import jp.mts.base.domain.model.DomainObject;

public class GroupJoinApplication extends DomainObject {

	private GroupJoinApplicationId id;
	private GroupId groupId;
	private MemberId applicantMemberId;

	public GroupJoinApplication(
			GroupJoinApplicationId id,
			GroupId groupId, 
			MemberId applicantMemberId) {
		this.id = id;
		this.groupId = groupId;
		this.applicantMemberId = applicantMemberId;
	}
	
	public GroupJoinApplicationId id() {
		return id;
	}
	public GroupId groupId() {
		return groupId;
	}
	public MemberId applicationMemberId() {
		return applicantMemberId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroupJoinApplication other = (GroupJoinApplication) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
