package jp.mts.taskmanage.rest;

import jp.mts.base.rest.RestResponse;
import jp.mts.taskmanage.application.MemberAppService;
import jp.mts.taskmanage.rest.presentation.model.MemberList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MemberApi {
	
	@Autowired
	private MemberAppService memberAppService;
	
	@RequestMapping(value="/groups/{groupId}/members/", method=RequestMethod.GET)
	public RestResponse<MemberList> loadMembersInGroup(@PathVariable String groupId) {
		MemberList memberList = new MemberList();
		memberList.findByGroupId(groupId, memberAppService);
		return RestResponse.of(memberList);
	}

}
