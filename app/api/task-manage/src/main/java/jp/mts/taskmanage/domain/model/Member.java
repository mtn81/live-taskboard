package jp.mts.taskmanage.domain.model;


public class Member {
	private MemberId memberId;
	
	public Member(MemberId memberId) {
		super();
		this.memberId = memberId;
	}
	
	public MemberId memberId(){
		return memberId;
	}
	
	public Group createGroup(
			GroupId groupId, String groupName, String description){
		Group group = new Group(groupId, memberId, groupName, description);
		return group;
	}
	
	public GroupBelonging entryTo(Group group) {
		return new GroupBelonging(group.groupId(), memberId);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Member other = (Member) obj;
		if (memberId == null) {
			if (other.memberId != null)
				return false;
		} else if (!memberId.equals(other.memberId))
			return false;
		return true;
	}
	
	
	
}
