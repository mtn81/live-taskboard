package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.domain.model.group.Group;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class GroupSave {

	//input
	@NotBlank
	private String name;
	@Length(max=100)
	private String description;

	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	//output
	private Group group;

	public String getGroupId(){
		if(group == null) return null;
		return group.groupId().value();
	}
	public String getGroupName() {
		if(group == null) return null;
		return group.name();
	}

	//exec
	public void create(
			String memberId, 
			GroupAppService groupAppService){
		this.group = groupAppService.registerGroup(memberId, name, description);
	}
	public void modify(
			String memberId, 
			String groupId,
			GroupAppService groupAppService) {
		this.group = groupAppService.modifyGroup(memberId, groupId, name, description);
	}
	
}
