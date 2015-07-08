package jp.mts.taskmanage.rest.presentation.model;

import java.util.List;
import java.util.stream.Collectors;

import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.domain.model.Group;

public class GroupList {
	
	//output
	private List<Group> groups;

	public List<GroupView> getGroups(){
		List<GroupView> groupViews = groups.stream().map(
				g -> new GroupView(g)).collect(Collectors.toList());
		return groupViews;
	}

	public static class GroupView {
		private Group group;
		
		public GroupView(Group group) {
			this.group = group;
		}

		public String getGroupId() {
			return group.groupId().value();
		}
		public String getGroupName() {
			return group.name();
		}
	}

	public void loadBelongingGroups(String memberId, GroupAppService groupAppService) {
		this.groups = groupAppService.listBelonging(memberId);
	}
}
