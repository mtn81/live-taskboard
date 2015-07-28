package jp.mts.taskmanage.domain.model;

public class GroupBelonging {

	private GroupId groupId;
	private MemberId memberId;
	private boolean isAdmin;

	public GroupBelonging(
			GroupId groupId, 
			MemberId memberId,
			boolean isAdmin) {
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
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result
				+ ((memberId == null) ? 0 : memberId.hashCode());
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
		GroupBelonging other = (GroupBelonging) obj;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (memberId == null) {
			if (other.memberId != null)
				return false;
		} else if (!memberId.equals(other.memberId))
			return false;
		return true;
	}

	
}
