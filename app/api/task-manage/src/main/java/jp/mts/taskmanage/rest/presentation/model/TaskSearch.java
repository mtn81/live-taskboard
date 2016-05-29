package jp.mts.taskmanage.rest.presentation.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jp.mts.base.util.Assertions;
import jp.mts.taskmanage.application.query.TaskSearchQuery;
import jp.mts.taskmanage.application.query.TaskSearchQuery.SearchResult;
import jp.mts.taskmanage.domain.model.task.TaskStatus;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;


public class TaskSearch {
	
	//services
	private static TaskSearchQuery taskSearchQuery;

	public static void setTaskSearchQuery(TaskSearchQuery taskSearchQuery) {
		TaskSearch.taskSearchQuery = taskSearchQuery;
	}
	
	
	//output
	List<TaskSearchQuery.SearchResult> results;

	public List<TaskView> getTodo() {
		return results.stream()
				.filter(r -> r.getStatus() == TaskStatus.TODO)
				.map(TaskView::new).collect(Collectors.toList());
	}
	public List<TaskView> getDoing() {
		return results.stream()
				.filter(r -> r.getStatus() == TaskStatus.DOING)
				.map(TaskView::new).collect(Collectors.toList());
	}
	public List<TaskView> getDone() {
		return results.stream()
				.filter(r -> r.getStatus() == TaskStatus.DONE)
				.map(TaskView::new).collect(Collectors.toList());
	}

	//process
	public void searchTasks(
			String groupId, 
			String keyword, 
			String members) {

		Assertions.assertNonNull(groupId);
		
		results = taskSearchQuery.search(
				groupId, 
				keyword, 
				StringUtils.isEmpty(members) 
					? Lists.newArrayList() : Arrays.asList(members.split("\\,")));
	}
	
	public static class TaskView {
		private TaskSearchQuery.SearchResult result;

		public TaskView(SearchResult result) {
			this.result = result;
		}
		public String getTaskId() {
			return result.getTaskId();
		}
		public String getTaskName() {
			return result.getTaskName();
		}
		public Date getDeadline() {
			return result.getDeadline();
		}
		public String getAssigned() {
			return result.getAssigned();
		}
		public String getStatus() {
			return result.getStatus().name().toLowerCase();
		}
		public String getHilightMemo() {
			return result.getHilightMemo();
		}
	}


}
