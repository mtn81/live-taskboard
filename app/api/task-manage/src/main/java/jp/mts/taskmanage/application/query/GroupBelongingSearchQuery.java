package jp.mts.taskmanage.application.query;

import java.util.List;

public interface GroupBelongingSearchQuery {

	List<ByMemberResult> byMember(String memberId);
	ByMemberResult byMember(String memberId, String groupId);
	
	public static class ByMemberResult {
		public String groupId;
		public String groupName;
		public String description;
		public boolean isAdmin;

		public ByMemberResult(
				String groupId, 
				String groupName,
				String description, 
				boolean isAdmin) {
			this.groupId = groupId;
			this.groupName = groupName;
			this.description = description;
			this.isAdmin = isAdmin;
		}
	}
}
