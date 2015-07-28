package jp.mts.taskmanage.domain.model;

import static org.hamcrest.CoreMatchers.nullValue;
import jp.mts.base.domain.model.DomainObject;


public class Member extends DomainObject {
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
		domainEventPublisher.publish(new GroupCreated(groupId, memberId));
		return group;
	}
	
	public GroupBelonging entryAsAdministratorTo(Group group) {
		return new GroupBelonging(group.groupId(), memberId, true);
	}

	public boolean isOwnerFor(Group group) {
		return this.memberId.equals(group.ownerMemberId());
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
