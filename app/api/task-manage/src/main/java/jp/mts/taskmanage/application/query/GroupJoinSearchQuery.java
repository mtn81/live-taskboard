package jp.mts.taskmanage.application.query;

import java.util.Date;
import java.util.List;

import jp.mts.taskmanage.domain.model.GroupJoinApplicationStatus;

public interface GroupJoinSearchQuery {

	List<Result> byApplicant(String memberId);
	
	public static class Result {
		private String joinApplicationId;
		private String groupId;
		private String groupName;
		private String ownerName;
		private Date joinApplied;
		private GroupJoinApplicationStatus joinApplicationStatus;

		public Result(
				String joinApplicationId,
				String groupId, 
				String groupName, 
				String ownerName,
				Date joinApplied,
				GroupJoinApplicationStatus joinApplicationStatus) {
			this.joinApplicationId = joinApplicationId;
			this.groupId = groupId;
			this.groupName = groupName;
			this.ownerName = ownerName;
			this.joinApplied = joinApplied;
			this.joinApplicationStatus = joinApplicationStatus;
		}

		public String getJoinApplicationId() {
			return joinApplicationId;
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
