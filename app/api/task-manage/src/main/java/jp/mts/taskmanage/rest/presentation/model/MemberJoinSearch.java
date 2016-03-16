package jp.mts.taskmanage.rest.presentation.model;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jp.mts.taskmanage.application.query.GroupJoinSearchQuery;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery.ByAdminResult;

public class MemberJoinSearch {
	
	private static GroupJoinSearchQuery groupJoinSearchQuery;

	public static void setJoinGroupSearchQuery(GroupJoinSearchQuery groupJoinSearchQuery) {
		MemberJoinSearch.groupJoinSearchQuery = groupJoinSearchQuery;
	}
	
	//output
	private List<GroupJoinSearchQuery.ByAdminResult> memberSearchResults;
	
	public List<MemberJoinView> getMembers() {
		return memberSearchResults.stream()
				.map(m -> new MemberJoinView(m))
				.collect(Collectors.toList());
	}
	
	public static class MemberJoinView {
		private GroupJoinSearchQuery.ByAdminResult result;

		public MemberJoinView(ByAdminResult result) {
			this.result = result;
		}
		
		public String getMemberType() { return result.applicantType; }
		public String getMemberName() { return result.applicantName; }
		public String getGroupId() { return result.groupId; }
		public String getGroupName() { return result.groupName; }
		public Date getApplied() { return result.joinApplied; }
		public String getJoinId() { return result.joinApplicationId; }
	}

	//process
	public void searchAcceptableByAdmin(String memberId) {
		this.memberSearchResults = groupJoinSearchQuery.acceptableByAdmin(memberId);
	}
	public void searchRejectedByAdmin(String memberId) {
		this.memberSearchResults = groupJoinSearchQuery.rejectedByAdmin(memberId);
	}

}
