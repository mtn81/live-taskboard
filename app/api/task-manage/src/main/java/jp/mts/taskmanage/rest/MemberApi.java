package jp.mts.taskmanage.rest;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import jp.mts.base.rest.RestResponse;
import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.application.MemberAppService;
import jp.mts.taskmanage.rest.presentation.model.MemberList;
import jp.mts.taskmanage.rest.presentation.model.MemberRoleChange;

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
		MemberRoleChange.setGroupAppService(groupAppService);
	}
	
	@RequestMapping(
			value="/groups/{groupId}/members/", 
			method=RequestMethod.GET)
	public RestResponse<MemberList> loadMembersInGroup(@PathVariable String groupId) {
		MemberList memberList = new MemberList();
		memberList.findByGroupId(groupId);
		return RestResponse.of(memberList);
	}
	@RequestMapping(
			value="/members/{memberId}", 
			params="change_admin", 
			method=RequestMethod.PUT)
	public RestResponse<MemberRoleChange> changeToAdmin(
			@PathVariable String memberId,
			@RequestBody @Valid MemberRoleChange memberRoleChange) {
		
		memberRoleChange.changeToAdmin(memberId);
		return RestResponse.of(memberRoleChange);
	}
	@RequestMapping(
			value="/members/{memberId}", 
			params="change_normal", 
			method=RequestMethod.PUT)
	public RestResponse<MemberRoleChange> changeToNormal(
			@PathVariable String memberId,
			@RequestBody @Valid MemberRoleChange memberRoleChange) {
		
		memberRoleChange.changeToNormal(memberId);
		return RestResponse.of(memberRoleChange);
	}

}
