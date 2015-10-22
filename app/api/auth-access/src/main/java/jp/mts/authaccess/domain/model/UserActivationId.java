package jp.mts.authaccess.domain.model;

import java.util.UUID;

public class UserActivationId {

	private String value;
	
	public UserActivationId() {
		this.value = UUID.randomUUID().toString();
	}
	
	public UserActivationId(String value) {
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
		UserActivationId other = (UserActivationId) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	

}