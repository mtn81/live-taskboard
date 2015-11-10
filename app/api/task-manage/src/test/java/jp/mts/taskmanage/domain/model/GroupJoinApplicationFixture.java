package jp.mts.taskmanage.domain.model;

public class GroupJoinApplicationFixture {

	private GroupJoinApplication target;
	
	public GroupJoinApplicationFixture() {
		this("g01", "m01");
	}
	public GroupJoinApplicationFixture(String groupId, String memberId) {
		this("a01", groupId, memberId);
	}
	public GroupJoinApplicationFixture(String applicationId, String groupId, String memberId) {
		target = new GroupJoinApplication(
				new GroupJoinApplicationId(applicationId), 
				new GroupId(groupId), 
				new MemberId(memberId));
	}
	public GroupJoinApplicationFixture setStatus(GroupJoinApplicationStatus status) {
		target.setStatus(status);
		return this;
	}
	
	public GroupJoinApplication get() {
		return target;
	}
}
