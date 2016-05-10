package jp.mts.taskmanage.rest;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import jp.mts.base.rest.RestResponse;
import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.application.query.TaskSearchQuery;
import jp.mts.taskmanage.rest.aspect.MemberContext;
import jp.mts.taskmanage.rest.authorize.GroupBelong;
import jp.mts.taskmanage.rest.presentation.model.TaskDetailLoad;
import jp.mts.taskmanage.rest.presentation.model.TaskDetailSave;
import jp.mts.taskmanage.rest.presentation.model.TaskRemove;
import jp.mts.taskmanage.rest.presentation.model.TaskSave;
import jp.mts.taskmanage.rest.presentation.model.TaskSearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TaskApi {

	@Autowired
	private TaskAppService taskAppService;
	@Autowired
	private TaskSearchQuery taskSearchQuery;
	
	@PostConstruct
	public void initialize(){
		TaskSearch.setTaskSearchQuery(taskSearchQuery);
		TaskDetailLoad.setTaskAppService(taskAppService);
		TaskDetailSave.setTaskAppService(taskAppService);
	}

	@RequestMapping(
			value="/groups/{groupId}/tasks/search", 
			method=RequestMethod.GET)
	public RestResponse<TaskSearch> searchTasksInGroup(
			@PathVariable("groupId") @GroupBelong String groupId,
			@RequestParam("keyword") String keyword,
			@RequestParam("members") String members) {
		
		TaskSearch taskSearch = new TaskSearch();
		taskSearch.searchTasks(groupId, keyword, members);
		return RestResponse.of(taskSearch);
	}
	@RequestMapping(
			value="/groups/{groupId}/tasks/{taskId}", 
			method=RequestMethod.GET)
	public RestResponse<TaskDetailLoad> loadTaskDetail(
			@PathVariable String groupId, 
			@PathVariable String taskId) {
		
		TaskDetailLoad taskDetailLoad = new TaskDetailLoad();
		taskDetailLoad.load(groupId, taskId);
		return RestResponse.of(taskDetailLoad);
	}

	@RequestMapping(
			value="/groups/{groupId}/tasks/", 
			method=RequestMethod.POST)
	public RestResponse<TaskSave> registerTask(
			@PathVariable("groupId") @GroupBelong String groupId,
			@RequestBody TaskSave taskSave) {
		
		taskSave.create(groupId, taskAppService);
		return RestResponse.of(taskSave);
	}
	
	@RequestMapping(
			value="/groups/{groupId}/tasks/{taskId}", 
			method=RequestMethod.PUT,
			params="!detail")
	public RestResponse<TaskSave> modifyTask(
			@PathVariable @GroupBelong String groupId, 
			@PathVariable String taskId, 
			@RequestBody TaskSave taskSave) {
		
		taskSave.update(MemberContext.memberId(), groupId, taskId, taskAppService);
		return RestResponse.of(taskSave);
	}

	@RequestMapping(
			value="/groups/{groupId}/tasks/{taskId}", 
			method=RequestMethod.PUT,
			params={"detail", "!validate"})
	public RestResponse<TaskDetailSave> modifyTaskDetail(
			@PathVariable @GroupBelong String groupId, 
			@PathVariable String taskId, 
			@RequestBody @Valid TaskDetailSave taskDetailSave,
			BindingResult result) {
		
		if (result.hasErrors()) return RestResponse.of(result);
		taskDetailSave.update(MemberContext.memberId(), groupId, taskId);
		return RestResponse.of(taskDetailSave);
	}
	@RequestMapping(
			value="/groups/{groupId}/tasks/{taskId}", 
			method=RequestMethod.PUT,
			params={"detail", "validate"})
	public RestResponse<TaskDetailSave> validateTaskDetail(
			@PathVariable @GroupBelong String groupId, 
			@PathVariable String taskId, 
			@RequestBody @Valid TaskDetailSave taskDetailSave,
			BindingResult result) {

		if (result.hasErrors()) return RestResponse.of(result);
		return RestResponse.of(taskDetailSave);
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
