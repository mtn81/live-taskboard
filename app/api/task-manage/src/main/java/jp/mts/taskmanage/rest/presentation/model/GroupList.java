package jp.mts.taskmanage.rest.presentation.model;

import java.util.List;
import java.util.stream.Collectors;

import jp.mts.taskmanage.application.query.GroupBelongingSearchQuery;
import jp.mts.taskmanage.application.query.GroupBelongingSearchQuery.ByMemberResult;

public class GroupList {
	
	//required services
	private static GroupBelongingSearchQuery groupBelongingSearchQuery;

	public static void setGroupBelongingSearchQuery(
			GroupBelongingSearchQuery groupBelongingSearchQuery) {
		GroupList.groupBelongingSearchQuery = groupBelongingSearchQuery;
	}

	//output
	private List<ByMemberResult> groups;

	public List<GroupView> getGroups(){
		List<GroupView> groupViews = groups.stream().map(
				g -> new GroupView(g)).collect(Collectors.toList());
		return groupViews;
	}

	public static class GroupView {
		private ByMemberResult group;
		
		public GroupView(ByMemberResult group) {
			this.group = group;
		}

		public String getGroupId() {
			return group.groupId;
		}
		public String getGroupName() {
			return group.groupName;
		}
		public boolean isAdmin() {
			return group.isAdmin;
		}
	}

	public void loadBelongingGroups(String memberId) {
		this.groups = groupBelongingSearchQuery.byMember(memberId);
	}
}
