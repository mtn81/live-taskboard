package jp.mts.taskmanage.domain.model;


public class Group {
	
	private GroupId groupId;
	private MemberId ownerMemberId;
	private String name;
	private String description;
	private State state;

	public Group(GroupId groupId, MemberId ownerMemberId, String name, String description) {
		super();
		this.groupId = groupId;
		this.ownerMemberId = ownerMemberId;
		setName(name);
		setDescription(description);
		setState(State.CREATING);
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
	public State state() {
		return state;
	}
	public void changeToAvailable() {
		setState(State.AVAILABLE);
	}
	
	void setName(String name) {
		if(name == null) throw new IllegalArgumentException();
		this.name = name;
	}
	void setDescription(String description) {
		this.description = description;
	}
	void setState(State state) {
		this.state = state;
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

	public enum State {
		CREATING,
		AVAILABLE,
		;
	}
}
