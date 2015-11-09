package jp.mts.taskmanage.domain.model;

import jp.mts.base.domain.model.DomainObject;

public class GroupJoinApplicationId extends DomainObject {
	
	private String value;

	public GroupJoinApplicationId(String value) {
		this.value = value;
	}
	
	public String value() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		GroupJoinApplicationId other = (GroupJoinApplicationId) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	

}
