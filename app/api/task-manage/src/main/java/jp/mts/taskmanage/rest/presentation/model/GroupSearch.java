package jp.mts.taskmanage.rest.presentation.model;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jp.mts.taskmanage.application.query.GroupSearchQuery;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationStatus;

public class GroupSearch {
	
	private static GroupSearchQuery groupSearchQuery;

	public static void setGroupSearchQuery(GroupSearchQuery groupSearchQuery) {
		GroupSearch.groupSearchQuery = groupSearchQuery;
	}
	
	//output
	private List<GroupSearchQuery.Result> groupSearchResults;
	
	public List<GroupView> getGroups() {
		return groupSearchResults.stream()
				.map(g -> new GroupView(g))
				.collect(Collectors.toList());
	}

	public static class GroupView {

		private GroupSearchQuery.Result groupSearchResult;
		
		public GroupView(GroupSearchQuery.Result groupSearchResult) {
			this.groupSearchResult = groupSearchResult;
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
	public void searchNotJoinAppliedGroupsByName(
			String memberId, 
			String groupName) {

		this.groupSearchResults = groupSearchQuery.notJoinAppliedByName(memberId, groupName);
	}

	public void searchJoinAppliedGroups(String memberId) {
		this.groupSearchResults = groupSearchQuery.joinApplied(memberId);
	}

}
