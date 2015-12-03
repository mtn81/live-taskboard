package jp.mts.taskmanage.rest;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import jp.mts.base.rest.RestResponse;
import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.application.MemberAppService;
import jp.mts.taskmanage.rest.authorize.GroupAdmin;
import jp.mts.taskmanage.rest.authorize.GroupBelong;
import jp.mts.taskmanage.rest.presentation.model.MemberList;
import jp.mts.taskmanage.rest.presentation.model.MemberChange;
import jp.mts.taskmanage.rest.presentation.model.MemberRemove;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MemberApi {
	
	@Autowired
	private MemberAppService memberAppService;
	@Autowired
	private GroupAppService groupAppService;
	
	@PostConstruct
	public void initialize() {
		MemberList.setMemberAppService(memberAppService);
		MemberChange.setGroupAppService(groupAppService);
		MemberRemove.setGroupAppService(groupAppService);
	}
	
	@RequestMapping(
			value="/groups/{groupId}/members/", 
			method=RequestMethod.GET)
	public RestResponse<MemberList> loadMembersInGroup(
			@PathVariable @GroupBelong String groupId) {

		MemberList memberList = new MemberList();
		memberList.findByGroupId(groupId);
		return RestResponse.of(memberList);
	}
	@RequestMapping(
			value="/groups/{groupId}/members/{memberId}", 
			method=RequestMethod.PUT)
	public RestResponse<MemberChange> changeMember(
			@PathVariable String memberId,
			@PathVariable @GroupAdmin String groupId,
			@RequestBody @Valid MemberChange memberChange) {
		
		memberChange.change(groupId, memberId);
		return RestResponse.of(memberChange);
	}
	@RequestMapping(
			value="/groups/{groupId}/members/{memberId}", 
			method=RequestMethod.DELETE)
	public RestResponse<MemberRemove> removeMember(
			@PathVariable String memberId,
			@PathVariable @GroupAdmin String groupId) {

		MemberRemove memberRemove = new MemberRemove();
		memberRemove.remove(groupId, memberId);
		return RestResponse.of(memberRemove);
	}

}
