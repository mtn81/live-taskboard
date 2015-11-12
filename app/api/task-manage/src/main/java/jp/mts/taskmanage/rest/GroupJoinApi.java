package jp.mts.taskmanage.rest;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import jp.mts.base.rest.RestResponse;
import jp.mts.taskmanage.application.GroupJoinAppService;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery;
import jp.mts.taskmanage.rest.presentation.model.AcceptableMemberJoinSearch;
import jp.mts.taskmanage.rest.presentation.model.GroupJoinApply;
import jp.mts.taskmanage.rest.presentation.model.GroupJoinCancel;
import jp.mts.taskmanage.rest.presentation.model.GroupJoinSearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
		AcceptableMemberJoinSearch.setJoinGroupSearchQuery(groupJoinSearchQuery);
	}

	@RequestMapping(
			value="/members/{applicantId}/group_joins/", 
			method=RequestMethod.POST)
	public RestResponse<GroupJoinApply> apply(
			@PathVariable("applicantId") String applicantId,
			@RequestBody @Valid GroupJoinApply groupJoinApply){
		
		groupJoinApply.apply(applicantId);
		return RestResponse.of(groupJoinApply);
	}

	@RequestMapping(
			value="/members/{applicantId}/group_joins/{joinApplicationId}", 
			method=RequestMethod.DELETE)
	public RestResponse<GroupJoinCancel> cancel(
			@PathVariable("applicantId") String applicantId,
			@PathVariable("joinApplicationId") String joinApplicationId){
		
		GroupJoinCancel groupJoinCancel = new GroupJoinCancel();
		groupJoinCancel.cancel(applicantId, joinApplicationId);
		return RestResponse.of(groupJoinCancel);
	}

	
	@RequestMapping(
			value="/members/{memberId}/group_joins/", 
			method=RequestMethod.GET)
	public RestResponse<GroupJoinSearch> searchAppliedGroups(
			@PathVariable("memberId") String memberId) {
		
		GroupJoinSearch groupSearch = new GroupJoinSearch();
		groupSearch.searchByApplicant(memberId);
		return RestResponse.of(groupSearch);
	}
	
	
	@RequestMapping(
			value="/group_joins/search", 
			params="acceptable",
			method=RequestMethod.GET)
	public RestResponse<AcceptableMemberJoinSearch> searchAcceptableGroupJoinApplications(
			@RequestParam("memberId") String memberId) {
		
		AcceptableMemberJoinSearch search = new AcceptableMemberJoinSearch();
		search.searchAcceptableByAdmin(memberId);
		return RestResponse.of(search);
	}

}
