package jp.mts.taskmanage.domain.model;

import java.util.Collection;

public class MemberBuilder {

	private Member member;

	public MemberBuilder(Member member) {
		this.member = member;
	}
	
	public MemberBuilder addGroupBelonging(GroupBelonging groupBelonging) {
		member.addGroupBelonging(groupBelonging);
		return this;
	}
	public MemberBuilder addGroupBelongings(Collection<GroupBelonging> groupBelongings) {
		member.addGroupBelongings(groupBelongings);
		return this;
	}
	
	public Member get() {
		return member;
	}
	
}
