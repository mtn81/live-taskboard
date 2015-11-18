package jp.mts.taskmanage.domain.model;

public class MemberFixture {
	private Member member;
	
	public MemberFixture(){
		this("m01", "taro");
	}
	public MemberFixture(String memberId){
		this(memberId, "taro");
	}
	public MemberFixture(String memberId, String name){
		member = new Member(new MemberId(memberId), name);
	}
	public MemberFixture setName(String name) {
		member.setName(name);
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
