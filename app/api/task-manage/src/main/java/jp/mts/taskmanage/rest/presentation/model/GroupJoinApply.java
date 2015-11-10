package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.domain.model.GroupJoinApplication;

public class GroupJoinApply {

	//input
	private String applicantMemberId;

	public void setApplicantMemberId(String applicantMemberid) {
		this.applicantMemberId = applicantMemberid;
	}

	//output
	private GroupJoinApplication groupJoin;

	public String getJoinId() {
		return groupJoin.id().value();
	}

	public void apply(String groupId, GroupAppService groupAppService) {
		this.groupJoin = groupAppService.applyJoin(groupId, applicantMemberId);
	}
	
}
