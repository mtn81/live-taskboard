package jp.mts.taskmanage.domain.model;

public class MemberFixture {
	private Member member;
	
	public MemberFixture(){
		member = new Member(new MemberId("m01"));
	}
	
	public Member get(){
		return member;
	}
}
