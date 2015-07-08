package jp.mts.taskmanage.domain.model;

public interface MemberRepository {
	
	void save(Member member);
	public Member findById(MemberId memberId);

}
