package jp.mts.taskmanage.rest.presentation.model;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jp.mts.taskmanage.application.query.GroupJoinSearchQuery;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery.AcceptableByAdminResult;

public class AcceptableMemberJoinSearch {
	
	private static GroupJoinSearchQuery groupJoinSearchQuery;

	public static void setJoinGroupSearchQuery(GroupJoinSearchQuery groupJoinSearchQuery) {
		AcceptableMemberJoinSearch.groupJoinSearchQuery = groupJoinSearchQuery;
	}
	
	//output
	private List<GroupJoinSearchQuery.AcceptableByAdminResult> memberSearchResults;
	
	public List<MemberJoinView> getMembers() {
		return memberSearchResults.stream()
				.map(m -> new MemberJoinView(m))
				.collect(Collectors.toList());
	}
	
	public static class MemberJoinView {
		private GroupJoinSearchQuery.AcceptableByAdminResult result;

		public MemberJoinView(AcceptableByAdminResult result) {
			this.result = result;
		}
		
		public String getMemberId() { return result.applicantId; }
		public String getGroupName() { return result.groupName; }
		public Date getApplied() { return result.joinApplied; }
		public String getJoinId() { return result.joinApplicationId; }
	}

	//process
	public void searchAcceptableByAdmin(String memberId) {
		this.memberSearchResults = groupJoinSearchQuery.acceptableByAdmin(memberId);
	}

}
