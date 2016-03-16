package jp.mts.taskmanage.rest.presentation.model;

import java.util.List;
import java.util.stream.Collectors;

import jp.mts.taskmanage.application.query.GroupJoinSearchQuery;

public class GroupSearch {
	
	private static GroupJoinSearchQuery groupJoinSearchQuery;
	
	public static void setGroupJoinSearchQuery(GroupJoinSearchQuery groupJoinSearchQuery) {
		GroupSearch.groupJoinSearchQuery = groupJoinSearchQuery;
	}

	//output
	private List<GroupJoinSearchQuery.NotJoinAppliedWithNameResult> groupSearchResults;
	
	public List<GroupView> getGroups() {
		return groupSearchResults.stream()
				.map(g -> new GroupView(g))
				.collect(Collectors.toList());
	}

	public static class GroupView {

		private GroupJoinSearchQuery.NotJoinAppliedWithNameResult groupSearchResult;
		
		public GroupView(GroupJoinSearchQuery.NotJoinAppliedWithNameResult groupSearchResult) {
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
		public String getOwnerType(){
			return groupSearchResult.getOwnerType();
		}
		public String getDescription() {
			return groupSearchResult.getDescription();
		}
	}

	//process
	public void searchNotJoinAppliedGroupsByName(
			String memberId, String groupName) {

		this.groupSearchResults = groupJoinSearchQuery.notJoinAppliedWithName(memberId, groupName);
	}


}
