package jp.mts.taskmanage.rest;

import jp.mts.base.rest.RestResponse;
import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.rest.presentation.model.TaskList;
import jp.mts.taskmanage.rest.presentation.model.TaskRemove;
import jp.mts.taskmanage.rest.presentation.model.TaskSave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TaskApi {

	@Autowired
	private TaskAppService taskAppService;

	@RequestMapping(value="/groups/{groupId}/tasks/", method=RequestMethod.GET)
	public RestResponse<TaskList> loadTasksInGroup(
			@PathVariable("groupId") String groupId) {
		
		TaskList taskList = new TaskList();
		taskList.loadTasks(groupId, taskAppService);
		return RestResponse.of(taskList);
	}

	@RequestMapping(value="/groups/{groupId}/tasks/", method=RequestMethod.POST)
	public RestResponse<TaskSave> registerTask(
			@PathVariable("groupId") String groupId,
			@RequestBody TaskSave taskSave) {
		
		taskSave.create(groupId, taskAppService);
		return RestResponse.of(taskSave);
	}
	
	@RequestMapping(value="/groups/{groupId}/tasks/{taskId}", method=RequestMethod.PUT)
	public RestResponse<TaskSave> modifyTask(
			@PathVariable String groupId, 
			@PathVariable String taskId, 
			@RequestBody TaskSave taskSave) {
		
		taskSave.update(groupId, taskId, taskAppService);
		
		return RestResponse.of(taskSave);
	}
	
	@RequestMapping(value="/groups/{groupId}/tasks/{taskId}", method=RequestMethod.DELETE)
	public RestResponse<TaskRemove> removeTask(
			@PathVariable("groupId") String groupId,
			@PathVariable("taskId") String taskId) {
		
		TaskRemove taskRemove = new TaskRemove();
		taskRemove.remove(groupId, taskId, taskAppService);
		return RestResponse.of(taskRemove);
	}
}
