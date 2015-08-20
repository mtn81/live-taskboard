package jp.mts.taskmanage.rest;

import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.rest.presentation.model.TaskList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TaskApi {

	@Autowired
	private TaskAppService taskAppService;

	@RequestMapping(value="/groups/{groupId}/tasks/")
	public RestResponse<TaskList> loadTasksInGroup(
			@PathVariable("groupId") String groupId) {
		
		TaskList taskList = new TaskList();
		taskList.loadTasks(groupId, taskAppService);
		return RestResponse.of(taskList);
	}
}
