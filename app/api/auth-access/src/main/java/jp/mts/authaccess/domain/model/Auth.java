package jp.mts.authaccess.domain.model;

public class Auth {
	
	private AuthId id;
	private UserId userId;

	public Auth(AuthId id, UserId userId) {
		setId(id);
		setUserId(userId);
	}
	
	public AuthId id(){
		return id;
	}
	public UserId userId(){
		return userId;
	}
	
	void setId(AuthId id){
		this.id = id;
	}
	void setUserId(UserId userId){
		this.userId = userId;
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
		Auth other = (Auth) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
