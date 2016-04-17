package jp.mts.taskmanage.domain.model.group;

import jp.mts.taskmanage.domain.model.group.Group;
import jp.mts.taskmanage.domain.model.member.MemberId;

public class GroupFixture {
	
	private Group group;
	
	public GroupFixture(){
		this("g01");
	}
	public GroupFixture(String groupId){
		this(groupId, "m01");
	}
	public GroupFixture(String groupId, String memberId){
		group = new Group(
				new GroupId(groupId), 
				new MemberId(memberId),
				"task group01", 
				"this is a test group");
	}
	public GroupFixture setName(String name) {
		group.setName(name);
		return this;
	}
	public GroupFixture setDescription(String description) {
		group.setDescription(description);
		return this;
	}
	
	public Group get(){
		return group;
	}
}
