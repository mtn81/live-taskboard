package jp.mts.taskmanage.domain.model;

public class GroupBelongingFixture {
	
	private GroupBelonging groupBelonging;
	
	public GroupBelongingFixture(){
		this("g01", "m01");
	}
	public GroupBelongingFixture(String groupId, String memberId){
		groupBelonging = new GroupBelonging(
				new GroupId(groupId), new MemberId(memberId), false);
	}
	public GroupBelongingFixture setAdmin(boolean isAdmin){
		groupBelonging.setAdmin(isAdmin);
		return this;
	}
	
	public GroupBelonging get(){
		return groupBelonging;
	}
}
