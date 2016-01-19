package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.query.GroupBelongingSearchQuery;
import jp.mts.taskmanage.application.query.GroupBelongingSearchQuery.ByMemberResult;

public class GroupLoad {
	
	//required services
	private static GroupBelongingSearchQuery groupBelongingSearchQuery;
	
	public static void setGroupBelongingSearchQuery(
			GroupBelongingSearchQuery groupBelongingSearchQuery) {
		GroupLoad.groupBelongingSearchQuery = groupBelongingSearchQuery;
	}

	//output
	private ByMemberResult result;
	
	public String getGroupId() {
		return result.groupId;
	}
	public String getName() {
		return result.groupName;
	}
	public String getDescription() {
		return result.description;
	}
	public boolean isAdmin() {
		return result.isAdmin;
	}

	public void loadBelongingGroup(String memberId, String groupId) {
		result = groupBelongingSearchQuery.byMember(memberId, groupId);
	}
}
