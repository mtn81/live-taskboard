package jp.mts.taskmanage.rest.presentation.model;

import org.hibernate.validator.constraints.NotBlank;

import jp.mts.taskmanage.application.GroupJoinAppService;
import jp.mts.taskmanage.domain.model.GroupJoinApplication;

public class MemberJoinAccept {

	//required services
	private static GroupJoinAppService groupJoinAppService;
	
	public static void setGroupJoinAppService(
			GroupJoinAppService groupJoinAppService) {
		MemberJoinAccept.groupJoinAppService = groupJoinAppService;
	}

	//input
	@NotBlank
	private String adminMemberId;
	
	public void setAdminMemberId(String adminMemberId) {
		this.adminMemberId = adminMemberId;
	}

	//output
	private GroupJoinApplication groupJoinApplication;
	
	public String getJoinId() {
		return groupJoinApplication.id().value();
	}

	//process
	public void reject(String joinApplicationId) {
		groupJoinApplication = groupJoinAppService.rejectJoin(joinApplicationId, adminMemberId);
	}

}
