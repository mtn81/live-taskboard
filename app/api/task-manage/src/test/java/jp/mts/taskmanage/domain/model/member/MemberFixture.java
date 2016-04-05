package jp.mts.taskmanage.domain.model.member;

import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.member.GroupBelonging;
import jp.mts.taskmanage.domain.model.member.Member;
import jp.mts.taskmanage.domain.model.member.MemberId;
import jp.mts.taskmanage.domain.model.member.MemberRegisterType;

public class MemberFixture {
	private Member member;
	
	public MemberFixture(){
		this("m01", "taro");
	}
	public MemberFixture(String memberId){
		this(memberId, "taro");
	}
	public MemberFixture(String memberId, String name){
		member = new Member(new MemberId(memberId), name, MemberRegisterType.PROPER);
	}
	public MemberFixture setName(String name) {
		member.setName(name);
		return this;
	}
	public MemberFixture setEmail(String email) {
		member.setEmail(email);
		return this;
	}
	public MemberFixture addGroupBelonging(String groupId, boolean admin) {
		member.addGroupBelonging(new GroupBelonging(new GroupId(groupId), admin));
		return this;
	}
	
	public Member get(){
		return member;
	}
}
