package jp.mts.taskmanage.rest.presentation.model;

import java.util.List;
import java.util.stream.Collectors;

import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.application.query.GroupSearchQuery;

public class GroupSearch {

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
	}

	//process
	public void searchByName(String groupName, GroupSearchQuery groupSearchQuery) {
		this.groupSearchResults = groupSearchQuery.byName(groupName);
	}

}
