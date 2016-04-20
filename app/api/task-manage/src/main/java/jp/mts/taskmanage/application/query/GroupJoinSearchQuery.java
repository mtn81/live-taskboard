package jp.mts.taskmanage.application.query;

import java.util.Date;
import java.util.List;

import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationStatus;

public interface GroupJoinSearchQuery {

	List<ByApplicantResult> byApplicant(String memberId);
	List<ByAdminResult> acceptableByAdmin(String memberId);
	List<ByAdminResult> rejectedByAdmin(String memberId);
	List<AppliableGroupResult> appliableGroups(String memberId, String groupName);
	
	public static class AppliableGroupResult {
		private String groupId;
		private String groupName;
		private String ownerName;
		private String ownerType;
		private String description;

		public AppliableGroupResult(
				String groupId, 
				String groupName, 
				String ownerName,
				String ownerType,
				String description) {
			this.groupId = groupId;
			this.groupName = groupName;
			this.ownerName = ownerName;
			this.ownerType = ownerType;
			this.description = description;
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
		public String getOwnerType() {
			return ownerType;
		}
		public String getDescription() {
			return description;
		}
	}
	
	public static class ByApplicantResult {
		public String joinApplicationId;
		public String groupId;
		public String groupName;
		public String ownerName;
		public String ownerType;
		public Date joinApplied;
		public GroupJoinApplicationStatus joinApplicationStatus;

		public ByApplicantResult(
				String joinApplicationId,
				String groupId, 
				String groupName, 
				String ownerName,
				String ownerType,
				Date joinApplied,
				GroupJoinApplicationStatus joinApplicationStatus) {
			this.joinApplicationId = joinApplicationId;
			this.groupId = groupId;
			this.groupName = groupName;
			this.ownerName = ownerName;
			this.ownerType = ownerType;
			this.joinApplied = joinApplied;
			this.joinApplicationStatus = joinApplicationStatus;
		}
	}

	public static class ByAdminResult {
		public String joinApplicationId;
		public String groupId;
		public String groupName;
		public String applicantId;
		public String applicantType;
		public String applicantName;
		public Date joinApplied;

		public ByAdminResult(
				String joinApplicationId,
				String groupId, 
				String groupName, 
				String applicantId, 
				String applicantType, 
				String applicantName,
				Date joinApplied) {
			super();
			this.joinApplicationId = joinApplicationId;
			this.groupId = groupId;
			this.groupName = groupName;
			this.applicantId = applicantId;
			this.applicantType = applicantType;
			this.applicantName = applicantName;
			this.joinApplied = joinApplied;
		}

	}
}
