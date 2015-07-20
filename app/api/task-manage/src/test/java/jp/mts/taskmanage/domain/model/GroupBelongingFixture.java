package jp.mts.taskmanage.domain.model;

public class GroupBelongingFixture {
	
	private GroupBelonging groupBelonging;
	
	public GroupBelongingFixture(){
		this("g01", "m01");
	}
	public GroupBelongingFixture(String groupId, String memberId){
		groupBelonging = new GroupBelonging(
				new GroupId(groupId), new MemberId(memberId));
	}
	
	public GroupBelonging get(){
		return groupBelonging;
	}
}
