package jp.mts.taskmanage.rest;

import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.rest.presentation.model.GroupSave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groups")
public class GroupsApi {
	
	@Autowired
	private GroupAppService groupAppService;

	@RequestMapping(value="/", method=RequestMethod.POST)
	public RestResponse<GroupSave> create(@RequestBody GroupSave group) {
		group.create(groupAppService);
		return RestResponse.of(group);
	}
	
}
