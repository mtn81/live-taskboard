package jp.mts.taskmanage.application.query;

import java.util.Date;
import java.util.List;

import jp.mts.taskmanage.domain.model.GroupJoinApplicationStatus;

public interface GroupSearchQuery {

	List<Result> notJoinAppliedByName(String memberId, String groupName);
	List<Result> joinApplied(String memberId);
	
	public static class Result {
		private String groupId;
		private String groupName;
		private String ownerName;
		private Date joinApplied;
		private GroupJoinApplicationStatus joinApplicationStatus;

		public Result(
				String groupId, 
				String groupName, 
				String ownerName,
				Date joinApplied,
				GroupJoinApplicationStatus joinApplicationStatus) {
			this.groupId = groupId;
			this.groupName = groupName;
			this.ownerName = ownerName;
			this.joinApplied = joinApplied;
			this.joinApplicationStatus = joinApplicationStatus;
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
		public Date getJoinApplied() {
			return joinApplied;
		}
		public GroupJoinApplicationStatus getJoinApplicationStatus() {
			return joinApplicationStatus;
		}
		
	}
}
