package jp.mts.taskmanage.rest;

import javax.annotation.PostConstruct;

import jp.mts.base.rest.RestResponse;
import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.rest.authorize.GroupBelong;
import jp.mts.taskmanage.rest.presentation.model.TaskDetailLoad;
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
	
	@PostConstruct
	public void initialize(){
		TaskDetailLoad.setTaskAppService(taskAppService);
	}

	@RequestMapping(value="/groups/{groupId}/tasks/", method=RequestMethod.GET)
	public RestResponse<TaskList> loadTasksInGroup(
			@PathVariable("groupId") @GroupBelong String groupId) {
		
		TaskList taskList = new TaskList();
		taskList.loadTasks(groupId, taskAppService);
		return RestResponse.of(taskList);
	}
	@RequestMapping(value="/groups/{groupId}/tasks/{taskId}", method=RequestMethod.GET)
	public RestResponse<TaskDetailLoad> loadTaskDetail(
			@PathVariable String groupId, 
			@PathVariable String taskId) {
		
		TaskDetailLoad taskDetailLoad = new TaskDetailLoad();
		taskDetailLoad.load(groupId, taskId);
		return RestResponse.of(taskDetailLoad);
	}

	@RequestMapping(value="/groups/{groupId}/tasks/", method=RequestMethod.POST)
	public RestResponse<TaskSave> registerTask(
			@PathVariable("groupId") @GroupBelong String groupId,
			@RequestBody TaskSave taskSave) {
		
		taskSave.create(groupId, taskAppService);
		return RestResponse.of(taskSave);
	}
	
	@RequestMapping(value="/groups/{groupId}/tasks/{taskId}", method=RequestMethod.PUT)
	public RestResponse<TaskSave> modifyTask(
			@PathVariable @GroupBelong String groupId, 
			@PathVariable String taskId, 
			@RequestBody TaskSave taskSave) {
		
		taskSave.update(groupId, taskId, taskAppService);
		
		return RestResponse.of(taskSave);
	}
	
	@RequestMapping(value="/groups/{groupId}/tasks/{taskId}", method=RequestMethod.DELETE)
	public RestResponse<TaskRemove> removeTask(
			@PathVariable("groupId") @GroupBelong String groupId,
			@PathVariable("taskId") String taskId) {
		
		TaskRemove taskRemove = new TaskRemove();
		taskRemove.remove(groupId, taskId, taskAppService);
		return RestResponse.of(taskRemove);
	}

}
