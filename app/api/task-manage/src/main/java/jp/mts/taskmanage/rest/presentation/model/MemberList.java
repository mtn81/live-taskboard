package jp.mts.taskmanage.rest.presentation.model;

import static java.util.stream.Collectors.toList;

import java.util.List;

import jp.mts.taskmanage.application.MemberAppService;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.Member;

public class MemberList {
	
	//required services
	private static MemberAppService memberAppService;
	
	public static void setMemberAppService(MemberAppService memberAppService) {
		MemberList.memberAppService = memberAppService;
	}

	private List<Member> members;
	private String groupId;

	public void findByGroupId(String groupId) {
		members = memberAppService.findMembersInGroup(groupId);
		this.groupId = groupId;
	}

	public List<MemberView> getMembers() {
		return members.stream()
				.map(member -> new MemberView(member, groupId))
				.collect(toList());
	}

	public static class MemberView {
		private Member member;
		private String groupId;

		public MemberView(Member member, String groupId) {
			this.member = member;
			this.groupId = groupId;
		}

		public String getMemberId() {
			return member.memberId().value();
		}

		public String getName() {
			return member.name();
		}
		
		public boolean isAdmin() {
			return member.belongsAsAdmin(new GroupId(groupId));
		}
	}

}
