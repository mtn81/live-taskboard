package jp.mts.taskmanage.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.rest.presentation.model.GroupList;
import jp.mts.taskmanage.rest.presentation.model.GroupRemove;
import jp.mts.taskmanage.rest.presentation.model.GroupSave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members/{memberId}")
public class GroupApi {
	
	@Autowired
	private GroupAppService groupAppService;

	@RequestMapping(value="/groups/", method=RequestMethod.POST)
	public RestResponse<GroupSave> createGroupOnMember(
			@PathVariable String memberId, 
			@RequestBody GroupSave groupSave) {
		groupSave.create(memberId, groupAppService);
		return RestResponse.of(groupSave);
	}
	@RequestMapping(value="/groups/{groupId}", method=RequestMethod.DELETE)
	public RestResponse<GroupRemove> removeGroupOnMember(
			@PathVariable String memberId, 
			@PathVariable String groupId) {
		
		GroupRemove groupRemove = new GroupRemove();
		groupRemove.remove(memberId, groupId, groupAppService);
		return RestResponse.of(groupRemove);
	}
	
	@RequestMapping(value="/groups/", params="type=belonging", method=RequestMethod.GET)
	public RestResponse<GroupList> listBelongingGroups(
			@PathVariable String memberId) {
		GroupList groupList = new GroupList();
		groupList.loadBelongingGroups(memberId, groupAppService);
		return RestResponse.of(groupList);
	}

	@RequestMapping(value="/groups/{groupId}/status", params="type=belonging", method=RequestMethod.GET)
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
}
