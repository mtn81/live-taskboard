package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.domain.model.GroupJoinApplication;

public class GroupJoinApply {

	//input
	private String applicantMemberid;

	public void setApplicantMemberid(String applicantMemberid) {
		this.applicantMemberid = applicantMemberid;
	}

	//output
	private GroupJoinApplication groupJoinApplication;

	public String getJoinApplicationId() {
		return groupJoinApplication.id().value();
	}

	public void apply(String groupId, GroupAppService groupAppService) {
		this.groupJoinApplication = groupAppService.applyJoin(groupId, applicantMemberid);
	}
	
}
