package jp.mts.taskmanage.rest.presentation.model;

import java.util.List;
import java.util.stream.Collectors;

import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.application.GroupAppService.GroupBelongingPair;
import jp.mts.taskmanage.domain.model.Group;

public class GroupList {
	
	//output
	private List<GroupBelongingPair> groups;

	public List<GroupView> getGroups(){
		List<GroupView> groupViews = groups.stream().map(
				g -> new GroupView(g)).collect(Collectors.toList());
		return groupViews;
	}

	public static class GroupView {
		private GroupBelongingPair group;
		
		public GroupView(GroupBelongingPair group) {
			this.group = group;
		}

		public String getGroupId() {
			return group.getGroup().groupId().value();
		}
		public String getGroupName() {
			return group.getGroup().name();
		}
		public boolean isAdmin() {
			return group.getGroupBelonging().isAdmin();
		}
	}

	public void loadBelongingGroups(String memberId, GroupAppService groupAppService) {
		this.groups = groupAppService.listBelonging(memberId);
	}
}
