package jp.mts.taskmanage.rest.presentation.model;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jp.mts.taskmanage.application.query.GroupJoinSearchQuery;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationStatus;

public class GroupJoinSearch {
	
	private static GroupJoinSearchQuery groupJoinSearchQuery;

	public static void setJoinGroupSearchQuery(GroupJoinSearchQuery groupJoinSearchQuery) {
		GroupJoinSearch.groupJoinSearchQuery = groupJoinSearchQuery;
	}
	
	//output
	private List<GroupJoinSearchQuery.ByApplicantResult> groupSearchResults;
	
	public List<GroupJoinView> getGroups() {
		return groupSearchResults.stream()
				.map(g -> new GroupJoinView(g))
				.collect(Collectors.toList());
	}

	public static class GroupJoinView {

		private GroupJoinSearchQuery.ByApplicantResult groupSearchResult;
		
		public GroupJoinView(GroupJoinSearchQuery.ByApplicantResult groupSearchResult) {
			this.groupSearchResult = groupSearchResult;
		}

		public String getJoinApplicationId() {
			return groupSearchResult.joinApplicationId;
		}
		public String getGroupId(){
			return groupSearchResult.groupId;
		}
		public String getGroupName(){
			return groupSearchResult.groupName;
		}
		public String getOwner(){
			return groupSearchResult.ownerName;
		}
		public String getOwnerType(){
			return groupSearchResult.ownerType;
		}
		public Date getJoinApplied(){
			return groupSearchResult.joinApplied;
		}
		public GroupJoinApplicationStatus getJoinApplicationStatus() {
			return groupSearchResult.joinApplicationStatus;
		}
	}

	//process
	public void searchByApplicant(String memberId) {
		this.groupSearchResults = groupJoinSearchQuery.byApplicant(memberId);
	}

}
