package jp.mts.taskmanage.rest.presentation.model;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jp.mts.taskmanage.application.query.GroupJoinSearchQuery;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationStatus;

public class GroupJoinSearch {
	
	private static GroupJoinSearchQuery groupJoinSearchQuery;

	public static void setJoinGroupSearchQuery(GroupJoinSearchQuery groupJoinSearchQuery) {
		GroupJoinSearch.groupJoinSearchQuery = groupJoinSearchQuery;
	}
	
	//output
	private List<GroupJoinSearchQuery.Result> groupSearchResults;
	
	public List<GroupJoinView> getGroups() {
		return groupSearchResults.stream()
				.map(g -> new GroupJoinView(g))
				.collect(Collectors.toList());
	}

	public static class GroupJoinView {

		private GroupJoinSearchQuery.Result groupSearchResult;
		
		public GroupJoinView(GroupJoinSearchQuery.Result groupSearchResult) {
			this.groupSearchResult = groupSearchResult;
		}

		public String getJoinApplicationId() {
			return groupSearchResult.getJoinApplicationId();
		}
		public String getGroupId(){
			return groupSearchResult.getGroupId();
		}
		public String getGroupName(){
			return groupSearchResult.getGroupName();
		}
		public String getOwner(){
			return groupSearchResult.getOwnerName();
		}
		public Date getJoinApplied(){
			return groupSearchResult.getJoinApplied();
		}
		public GroupJoinApplicationStatus getJoinApplicationStatus() {
			return groupSearchResult.getJoinApplicationStatus();
		}
	}

	//process
	public void searchByApplicant(String memberId) {
		this.groupSearchResults = groupJoinSearchQuery.byApplicant(memberId);
	}

}
