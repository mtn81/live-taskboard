package jp.mts.taskmanage.rest;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import jp.mts.base.rest.RestResponse;
import jp.mts.taskmanage.application.GroupJoinAppService;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery;
import jp.mts.taskmanage.rest.presentation.model.GroupJoinApply;
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
	private GroupJoinSearchQuery groupSearchQuery;
	
	@PostConstruct
	public void initialize() {
		GroupJoinApply.setGroupJoinAppService(groupJoinAppService);
		GroupJoinSearch.setJoinGroupSearchQuery(groupSearchQuery);
	}

	@RequestMapping(value="/members/{applicantId}/group_joins/", method=RequestMethod.POST)
	public RestResponse<GroupJoinApply> apply(
			@PathVariable("applicantId") String applicantId,
			@RequestBody @Valid GroupJoinApply groupJoinApply){
		
		groupJoinApply.apply(applicantId);
		return RestResponse.of(groupJoinApply);
	}

	
	@RequestMapping(value="/members/{memberId}/group_joins/", method=RequestMethod.GET)
	public RestResponse<GroupJoinSearch> searchAppliedGroups(
			@PathVariable("memberId") String memberId) {
		
		GroupJoinSearch groupSearch = new GroupJoinSearch();
		groupSearch.searchByApplicant(memberId);
		return RestResponse.of(groupSearch);
	}
}
