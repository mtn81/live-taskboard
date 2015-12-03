package jp.mts.taskmanage.rest;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import jp.mts.base.rest.RestResponse;
import jp.mts.taskmanage.application.GroupJoinAppService;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery;
import jp.mts.taskmanage.rest.authorize.GroupAdmin;
import jp.mts.taskmanage.rest.authorize.Me;
import jp.mts.taskmanage.rest.presentation.model.GroupJoinApply;
import jp.mts.taskmanage.rest.presentation.model.GroupJoinCancel;
import jp.mts.taskmanage.rest.presentation.model.GroupJoinSearch;
import jp.mts.taskmanage.rest.presentation.model.GroupJoinAccept;
import jp.mts.taskmanage.rest.presentation.model.MemberJoinSearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GroupJoinApi {

	@Autowired
	private GroupJoinAppService groupJoinAppService;
	@Autowired
	private GroupJoinSearchQuery groupJoinSearchQuery;
	
	@PostConstruct
	public void initialize() {
		GroupJoinApply.setGroupJoinAppService(groupJoinAppService);
		GroupJoinSearch.setJoinGroupSearchQuery(groupJoinSearchQuery);
		GroupJoinCancel.setGroupJoinAppService(groupJoinAppService);
		MemberJoinSearch.setJoinGroupSearchQuery(groupJoinSearchQuery);
		GroupJoinAccept.setGroupJoinAppService(groupJoinAppService);
	}

	@RequestMapping(
			value="/members/{applicantId}/group_joins/", 
			method=RequestMethod.POST)
	public RestResponse<GroupJoinApply> apply(
			@PathVariable("applicantId") @Me String applicantId,
			@RequestBody @Valid GroupJoinApply groupJoinApply){
		
		groupJoinApply.apply(applicantId);
		return RestResponse.of(groupJoinApply);
	}

	@RequestMapping(
			value="/members/{applicantId}/group_joins/{joinApplicationId}", 
			method=RequestMethod.DELETE)
	public RestResponse<GroupJoinCancel> cancel(
			@PathVariable("applicantId") @Me String applicantId,
			@PathVariable("joinApplicationId") String joinApplicationId){
		
		GroupJoinCancel groupJoinCancel = new GroupJoinCancel();
		groupJoinCancel.cancel(applicantId, joinApplicationId);
		return RestResponse.of(groupJoinCancel);
	}

	@RequestMapping(
			value="/groups/{groupId}/group_joins/{joinApplicationId}/accept", 
			method=RequestMethod.PUT) 
	public RestResponse<GroupJoinAccept> accept(
			@PathVariable("groupId") @GroupAdmin String groupId,
			@PathVariable("joinApplicationId") String joinApplicationId){

		GroupJoinAccept groupJoinAccept = new GroupJoinAccept();
		groupJoinAccept.accept(groupId, joinApplicationId);
		return RestResponse.of(groupJoinAccept);
	}
	
	@RequestMapping(
			value="/groups/{groupId}/group_joins/{joinApplicationId}/reject", 
			method=RequestMethod.PUT) 
	public RestResponse<GroupJoinAccept> reject(
			@PathVariable("groupId") @GroupAdmin String groupId,
			@PathVariable("joinApplicationId") String joinApplicationId){
		
		GroupJoinAccept groupJoinAccept = new GroupJoinAccept();
		groupJoinAccept.reject(groupId, joinApplicationId);
		return RestResponse.of(groupJoinAccept);
	}
	
	@RequestMapping(
			value="/members/{memberId}/group_joins/", 
			method=RequestMethod.GET)
	public RestResponse<GroupJoinSearch> searchAppliedGroups(
			@PathVariable("memberId") @Me String memberId) {
		
		GroupJoinSearch groupSearch = new GroupJoinSearch();
		groupSearch.searchByApplicant(memberId);
		return RestResponse.of(groupSearch);
	}
	
	
	@RequestMapping(
			value="/members/{memberId}/acceptable_group_joins/search", 
			method=RequestMethod.GET)
	public RestResponse<MemberJoinSearch> searchAcceptableGroupJoinApplications(
			@PathVariable("memberId") @Me String memberId) {
		
		MemberJoinSearch search = new MemberJoinSearch();
		search.searchAcceptableByAdmin(memberId);
		return RestResponse.of(search);
	}

	@RequestMapping(
			value="/members/{memberId}/reject_group_joins/search", 
			method=RequestMethod.GET)
	public RestResponse<MemberJoinSearch> searchRejectedGroupJoinApplications(
			@PathVariable("memberId") @Me String memberId) {
		
		MemberJoinSearch search = new MemberJoinSearch();
		search.searchRejectedByAdmin(memberId);
		return RestResponse.of(search);
	}

}
