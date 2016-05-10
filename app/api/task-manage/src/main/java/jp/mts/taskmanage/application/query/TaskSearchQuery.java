package jp.mts.taskmanage.application.query;

import java.util.Date;
import java.util.List;

import jp.mts.taskmanage.domain.model.task.TaskStatus;

public interface TaskSearchQuery {

	List<SearchResult> search(String groupId, String keyword, List<String> memberIds);
	
	public static class SearchResult {
		
		private String taskId;
		private String taskName;
		private Date deadline;
		private String assigned;
		private TaskStatus status;
		private String descriptionHighlight;

		public SearchResult(
				String taskId, 
				String taskName, 
				Date deadline,
				String assigned, 
				TaskStatus status,
				String highlight) {
			super();
			this.taskId = taskId;
			this.taskName = taskName;
			this.deadline = deadline;
			this.assigned = assigned;
			this.status = status;
			this.descriptionHighlight = highlight;
		}

		public TaskStatus getStatus() {
			return status;
		}
		public String getTaskId() {
			return taskId;
		}
		public String getTaskName() {
			return taskName;
		}
		public Date getDeadline() {
			return deadline;
		}
		public String getAssigned() {
			return assigned;
		}
		public String getHilightMemo() {
			return descriptionHighlight;
		}
		
	}

}
