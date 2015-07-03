package jp.mts.taskmanage.domain.model;

public class GroupFixture {
	
	private Group group;
	
	public GroupFixture(){
		group = new Group(new GroupId("g01"), "task group01", "this is a test group");
	}
	
	public Group get(){
		return group;
	}
}
