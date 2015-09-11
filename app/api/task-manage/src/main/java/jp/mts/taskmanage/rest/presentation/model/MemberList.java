package jp.mts.taskmanage.rest.presentation.model;

import static java.util.stream.Collectors.toList;

import java.util.List;

import jp.mts.taskmanage.application.MemberAppService;
import jp.mts.taskmanage.domain.model.Member;

public class MemberList {
	
	private List<Member> members;

	public void findByGroupId(String groupId, MemberAppService memberAppService) {
		members = memberAppService.findMembersInGroup(groupId);
	}

	public List<MemberView> getMembers() {
		return members.stream()
				.map(member -> new MemberView(member))
				.collect(toList());
	}

	public static class MemberView {
		private Member member;

		public MemberView(Member member) {
			this.member = member;
		}

		public String getMemberId() {
			return member.memberId().value();
		}

		public String getName() {
			return member.name();
		}
	}

}
