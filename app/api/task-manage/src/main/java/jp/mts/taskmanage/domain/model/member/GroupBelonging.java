package jp.mts.taskmanage.domain.model.member;

import jp.mts.base.domain.model.DomainObject;
import jp.mts.taskmanage.domain.model.group.GroupId;

public class GroupBelonging extends DomainObject {

	private GroupId groupId;
	private boolean isAdmin;

	public GroupBelonging(
			GroupId groupId, 
			boolean isAdmin) {
		this.groupId = groupId;
		this.isAdmin = isAdmin;
	}
	
	public GroupId groupId(){
		return groupId;
	}
	public boolean isAdmin() {
		return isAdmin;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + (isAdmin ? 1231 : 1237);
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
		if (isAdmin != other.isAdmin)
			return false;
		return true;
	}
	
}
