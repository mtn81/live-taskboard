package jp.mts.taskmanage.domain.model;

public class MemberFixture {
	private Member member;
	
	public MemberFixture(){
		member = new Member(new MemberId("m01"));
	}
	public MemberFixture(String memberId){
		member = new Member(new MemberId(memberId));
	}
	
	public Member get(){
		return member;
	}
}
