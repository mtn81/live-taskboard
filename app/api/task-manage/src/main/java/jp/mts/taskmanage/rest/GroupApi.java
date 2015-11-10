package jp.mts.taskmanage.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import jp.mts.base.rest.RestResponse;
import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.application.query.GroupSearchQuery;
import jp.mts.taskmanage.rest.presentation.model.GroupJoinApply;
import jp.mts.taskmanage.rest.presentation.model.GroupList;
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
	private GroupSearchQuery groupSearchQuery;
	
	@PostConstruct
	public void initialize() {
		GroupSearch.setGroupSearchQuery(groupSearchQuery);
	}

	@RequestMapping(value="/members/{memberId}/groups/", method=RequestMethod.POST)
	public RestResponse<GroupSave> createGroupOnMember(
			@PathVariable String memberId, 
			@RequestBody GroupSave groupSave) {
		groupSave.create(memberId, groupAppService);
		return RestResponse.of(groupSave);
	}
	@RequestMapping(value="/members/{memberId}/groups/{groupId}", method=RequestMethod.DELETE)
	public RestResponse<GroupRemove> removeGroupOnMember(
			@PathVariable String memberId, 
			@PathVariable String groupId) {
		
		GroupRemove groupRemove = new GroupRemove();
		groupRemove.remove(memberId, groupId, groupAppService);
		return RestResponse.of(groupRemove);
	}
	
	@RequestMapping(value="/members/{memberId}/groups/", params="type=belonging", method=RequestMethod.GET)
	public RestResponse<GroupList> listBelongingGroups(
			@PathVariable String memberId) {
		GroupList groupList = new GroupList();
		groupList.loadBelongingGroups(memberId, groupAppService);
		return RestResponse.of(groupList);
	}
	
	@RequestMapping(value="/members/{memberId}/groups/{groupId}/status", params="type=belonging", method=RequestMethod.GET)
	public void notifyGroupRegistered(
			@PathVariable String memberId,
			@PathVariable String groupId,
			HttpServletResponse response) throws IOException, InterruptedException {
		
		try(PrintWriter writer = response.getWriter()){
			response.setContentType("text/event-stream");
			response.setCharacterEncoding("UTF-8");
			writer.write("data: " + "" + "\n\n");
		}
		
	}

	@RequestMapping(value="/groups/search", params="not_join_applied", method=RequestMethod.GET)
	public RestResponse<GroupSearch> searchNotAppliedGroups(
			@RequestParam("applicantId") String applicantId,
			@RequestParam("groupName") String groupName) {
		
		GroupSearch groupSearch = new GroupSearch();
		groupSearch.searchNotJoinAppliedGroupsByName(applicantId, groupName);
		return RestResponse.of(groupSearch);
	}

	@RequestMapping(value="/groups/search", params="join_applied", method=RequestMethod.GET)
	public RestResponse<GroupSearch> searchAppliedGroups(
			@RequestParam("applicantId") String applicantId) {
		
		GroupSearch groupSearch = new GroupSearch();
		groupSearch.searchJoinAppliedGroups(applicantId);
		return RestResponse.of(groupSearch);
	}
	
	@RequestMapping(value="/groups/{groupId}/apply", method=RequestMethod.POST)
	public RestResponse<GroupJoinApply> applyJoin(
			@PathVariable String groupId,
			@RequestBody @Valid GroupJoinApply groupJoinApply){
		
		groupJoinApply.apply(groupId, groupAppService);
		return RestResponse.of(groupJoinApply);
	}
	
}
