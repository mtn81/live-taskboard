package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.GroupJoinAppService;
import jp.mts.taskmanage.domain.model.group.join.GroupJoin;

public class GroupJoinAccept {

	//required services
	private static GroupJoinAppService groupJoinAppService;
	
	public static void setGroupJoinAppService(
			GroupJoinAppService groupJoinAppService) {
		GroupJoinAccept.groupJoinAppService = groupJoinAppService;
	}

	//input

	//output
	private GroupJoin groupJoinApplication;
	
	public String getJoinId() {
		return groupJoinApplication.id().value();
	}

	//process
	public void reject(String groupId, String joinApplicationId) {
		groupJoinApplication = groupJoinAppService.rejectJoin(groupId, joinApplicationId);
	}
	public void accept(String groupId, String joinApplicationId) {
		groupJoinApplication = groupJoinAppService.acceptJoin(groupId, joinApplicationId);
	}


}
