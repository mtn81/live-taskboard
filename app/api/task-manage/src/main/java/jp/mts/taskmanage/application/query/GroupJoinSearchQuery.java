package jp.mts.taskmanage.application.query;

import java.util.Date;
import java.util.List;

import jp.mts.taskmanage.domain.model.GroupJoinApplicationStatus;

public interface GroupJoinSearchQuery {

	List<ByApplicantResult> byApplicant(String memberId);
	List<ByAdminResult> acceptableByAdmin(String memberId);
	List<ByAdminResult> rejectedByAdmin(String memberId);
	List<NotJoinAppliedWithNameResult> notJoinAppliedWithName(String memberId, String groupName);
	
	public static class NotJoinAppliedWithNameResult {
		private String groupId;
		private String groupName;
		private String ownerName;

		public NotJoinAppliedWithNameResult(
				String groupId, 
				String groupName, 
				String ownerName) {
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
	
	public static class ByApplicantResult {
		public String joinApplicationId;
		public String groupId;
		public String groupName;
		public String ownerName;
		public Date joinApplied;
		public GroupJoinApplicationStatus joinApplicationStatus;

		public ByApplicantResult(
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
	}

	public static class ByAdminResult {
		public String joinApplicationId;
		public String groupName;
		public String applicantId;
		public String applicantName;
		public Date joinApplied;

		public ByAdminResult(
				String joinApplicationId,
				String groupName, 
				String applicantId, 
				String applicantName,
				Date joinApplied) {
			super();
			this.joinApplicationId = joinApplicationId;
			this.groupName = groupName;
			this.applicantId = applicantId;
			this.applicantName = applicantName;
			this.joinApplied = joinApplied;
		}

	}
}
