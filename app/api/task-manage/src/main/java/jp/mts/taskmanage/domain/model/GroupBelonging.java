package jp.mts.taskmanage.domain.model;

public class GroupBelonging {

	private GroupId groupId;
	private MemberId memberId;

	public GroupBelonging(GroupId groupId, MemberId memberId) {
		super();
		this.groupId = groupId;
		this.memberId = memberId;
	}
	
	public GroupId groupId(){
		return groupId;
	}
	public MemberId memberId(){
		return memberId;
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
