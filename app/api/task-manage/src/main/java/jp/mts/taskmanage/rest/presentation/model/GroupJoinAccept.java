package jp.mts.taskmanage.rest.presentation.model;

import org.hibernate.validator.constraints.NotBlank;

import jp.mts.taskmanage.application.GroupJoinAppService;
import jp.mts.taskmanage.domain.model.GroupJoinApplication;

public class GroupJoinAccept {

	//required services
	private static GroupJoinAppService groupJoinAppService;
	
	public static void setGroupJoinAppService(
			GroupJoinAppService groupJoinAppService) {
		GroupJoinAccept.groupJoinAppService = groupJoinAppService;
	}

	//input

	//output
	private GroupJoinApplication groupJoinApplication;
	
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
