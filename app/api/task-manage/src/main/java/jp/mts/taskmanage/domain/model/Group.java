package jp.mts.taskmanage.domain.model;


public class Group {
	
	private GroupId groupId;
	private MemberId ownerMemberId;
	private String name;
	private String description;

	public Group(GroupId groupId, MemberId ownerMemberId, String name, String description) {
		super();
		this.groupId = groupId;
		this.ownerMemberId = ownerMemberId;
		setName(name);
		setDescription(description);
	}

	public GroupId groupId() {
		return groupId;
	}
	public MemberId ownerMemberId() {
		return ownerMemberId;
	}
	public String name() {
		return name;
	}
	public String description() {
		return description;
	}
	public void changeAttributes(String name, String description){
		setName(name);
		setDescription(description);
	}
	
	void setName(String name) {
		if(name == null) throw new IllegalArgumentException();
		this.name = name;
	}

	void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
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
		Group other = (Group) obj;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		return true;
	}
	
}
