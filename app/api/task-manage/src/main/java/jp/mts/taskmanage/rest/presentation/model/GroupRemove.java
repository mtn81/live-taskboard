package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.GroupAppService;

public class GroupRemove {
	
	private String groupId;
	
	public String getGroupId(){
		return groupId;
	}
		
	public void remove(
			String memberId, 
			String groupId, 
			GroupAppService groupAppService){
		
		groupAppService.remove(memberId, groupId);
		this.groupId = groupId;
	}
}
