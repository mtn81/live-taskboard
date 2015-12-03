package jp.mts.taskmanage.application.query;

import java.util.List;

public interface GroupBelongingSearchQuery {

	List<ByMemberResult> byMember(String memberId);
	ByMemberResult byMember(String memberId, String groupId);
	
	public static class ByMemberResult {
		public String groupId;
		public String groupName;
		public boolean isAdmin;

		public ByMemberResult(
				String groupId, 
				String groupName, 
				boolean isAdmin) {
			this.groupId = groupId;
			this.groupName = groupName;
			this.isAdmin = isAdmin;
		}
	}
}
