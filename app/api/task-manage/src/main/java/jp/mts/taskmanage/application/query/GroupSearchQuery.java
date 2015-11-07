package jp.mts.taskmanage.application.query;

import java.util.List;

public interface GroupSearchQuery {

	List<Result> byName(String groupName);
	
	public static class Result {
		private String groupId;
		private String groupName;
		private String ownerName;

		public Result(String groupId, String groupName, String ownerName) {
			this.groupId = groupId;
			this.groupName = groupName;
			this.ownerName = ownerName;
		}

		public String getGroupId() {
			return groupId;
		}
		public String getGroupName() {
			return groupName;
		}
		public String getOwnerName() {
			return ownerName;
		}
	}
}
