package jp.mts.taskmanage.rest;

import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.rest.presentation.model.GroupList;
import jp.mts.taskmanage.rest.presentation.model.GroupSave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members/{memberId}")
public class MemberGroupApi {
	
	@Autowired
	private GroupAppService groupAppService;

	@RequestMapping(value="/groups/", method=RequestMethod.POST)
	public RestResponse<GroupSave> createGroupOnMember(
			@PathVariable String memberId, @RequestBody GroupSave group) {
		group.create(memberId, groupAppService);
		return RestResponse.of(group);
	}
	
	@RequestMapping(value="/groups/", params="type=belonging", method=RequestMethod.GET)
	public RestResponse<GroupList> listBelongingGroups(
			@PathVariable String memberId) {
		GroupList groupList = new GroupList();
		groupList.loadBelongingGroups(memberId, groupAppService);
		return RestResponse.of(groupList);
	}
	
}
