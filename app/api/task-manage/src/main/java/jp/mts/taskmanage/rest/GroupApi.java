package jp.mts.taskmanage.rest;

import javax.annotation.PostConstruct;

import jp.mts.base.rest.RestResponse;
import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.application.query.GroupBelongingSearchQuery;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery;
import jp.mts.taskmanage.rest.authorize.Me;
import jp.mts.taskmanage.rest.presentation.model.GroupList;
import jp.mts.taskmanage.rest.presentation.model.GroupLoad;
import jp.mts.taskmanage.rest.presentation.model.GroupRemove;
import jp.mts.taskmanage.rest.presentation.model.GroupSave;
import jp.mts.taskmanage.rest.presentation.model.GroupSearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GroupApi {
	
	@Autowired
	private GroupAppService groupAppService;
	@Autowired
	private GroupJoinSearchQuery groupJoinSearchQuery;
	@Autowired
	private GroupBelongingSearchQuery groupBelongingSearchQuery;
	
	@PostConstruct
	public void initialize() {
		GroupSearch.setGroupJoinSearchQuery(groupJoinSearchQuery);
		GroupList.setGroupBelongingSearchQuery(groupBelongingSearchQuery);
		GroupLoad.setGroupBelongingSearchQuery(groupBelongingSearchQuery);
	}

	@RequestMapping(
			value="/members/{memberId}/groups/", 
			method=RequestMethod.POST)
	public RestResponse<GroupSave> createGroupOnMember(
			@PathVariable @Me String memberId, 
			@RequestBody GroupSave groupSave) {
		groupSave.create(memberId, groupAppService);
		return RestResponse.of(groupSave);
	}
	@RequestMapping(
			value="/members/{memberId}/groups/{groupId}", 
			method=RequestMethod.DELETE)
	public RestResponse<GroupRemove> removeGroupOnMember(
			@PathVariable @Me String memberId, 
			@PathVariable String groupId) {
		
		GroupRemove groupRemove = new GroupRemove();
		groupRemove.remove(memberId, groupId, groupAppService);
		return RestResponse.of(groupRemove);
	}
	
	@RequestMapping(
			value="/members/{memberId}/groups/{groupId}", 
			method=RequestMethod.GET)
	public RestResponse<GroupLoad> loadGroup(
			@PathVariable @Me String memberId,
			@PathVariable String groupId) {
		GroupLoad groupLoad = new GroupLoad();
		groupLoad.loadBelongingGroup(memberId, groupId);
		return RestResponse.of(groupLoad);
	}
	@RequestMapping(
			value="/members/{memberId}/groups/", 
			method=RequestMethod.GET)
	public RestResponse<GroupList> listBelongingGroups(
			@PathVariable @Me String memberId) {
		GroupList groupList = new GroupList();
		groupList.loadBelongingGroups(memberId);
		return RestResponse.of(groupList);
	}
	
	
	@RequestMapping(
			value="/members/{applicantId}/not_join_applied_groups/search", 
			method=RequestMethod.GET)
	public RestResponse<GroupSearch> searchNotAppliedGroups(
			@PathVariable("applicantId") @Me String applicantId,
			@RequestParam("groupName") String groupName) {
		
		GroupSearch groupSearch = new GroupSearch();
		groupSearch.searchNotJoinAppliedGroupsByName(applicantId, groupName);
		return RestResponse.of(groupSearch);
	}
	
}
