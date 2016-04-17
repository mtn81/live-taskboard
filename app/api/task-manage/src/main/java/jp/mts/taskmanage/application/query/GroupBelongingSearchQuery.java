package jp.mts.taskmanage.application.query;

import java.util.List;

public interface GroupBelongingSearchQuery {

	List<GroupSummary> byMember(String memberId);
	
	public static class GroupSummary {
		public String groupId;
		public String groupName;
		public boolean isAdmin;

		public GroupSummary(
				String groupId, 
				String groupName,
				boolean isAdmin) {
			this.groupId = groupId;
			this.groupName = groupName;
			this.isAdmin = isAdmin;
		}
	}
}
