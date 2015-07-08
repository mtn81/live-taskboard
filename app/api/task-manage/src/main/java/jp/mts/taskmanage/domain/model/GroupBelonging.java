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
	
}
